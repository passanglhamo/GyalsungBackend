package com.microservice.erp.domain.tasks.iam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.domain.dto.ScreenDto;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.helper.Permission;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.services.impl.RoleWiseAccessPermissionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;

import java.math.BigInteger;

public class Login extends TokenizerTask {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private RoleWiseAccessPermissionService roleWiseAccessPermissionService;
    private Boolean isNDILogin;
    private IRoleRepository iRoleRepository;


    public Login(UserRepository repository, PasswordEncoder encoder, RoleWiseAccessPermissionService roleWiseAccessPermissionService, Boolean isNDILogin, IRoleRepository iRoleRepository, Property... properties) {
        super(properties);
        this.repository = repository;
        this.encoder = encoder;
        this.roleWiseAccessPermissionService = roleWiseAccessPermissionService;
        this.isNDILogin = isNDILogin;
        this.iRoleRepository = iRoleRepository;
    }

    public Login(UserRepository repository, PasswordEncoder encoder, RoleWiseAccessPermissionService roleWiseAccessPermissionService, Boolean isNDILogin, IRoleRepository iRoleRepository, String username) {
        this(repository, encoder, roleWiseAccessPermissionService, isNDILogin, iRoleRepository, new Property("username", username));
    }

    public Login(UserRepository repository, PasswordEncoder encoder, RoleWiseAccessPermissionService roleWiseAccessPermissionService, Boolean isNDILogin, IRoleRepository iRoleRepository, LoginRequest request) {
        this(repository, encoder, roleWiseAccessPermissionService, isNDILogin, iRoleRepository, request.getRow().getProperties().toArray(new Property[0]));
    }

    @Override
    public Response getMessage() {
        try {
            Message saved = super.getMessage();
            Map<String, Object> data = Message.unmarshal(new TypeReference<Map<String, Object>>() {
            }, saved.getPayload());
            saved = new LoginRequest();
            saved.unmarshallingFromMap(data, true);
            return (Response) saved;
        } catch (IOException e) {
        }
        return super.getMessage();
    }

    @Override
    protected Calendar getTimeToLive() {
        LoginRequest request = (LoginRequest) getMessage();
        Calendar cal = Calendar.getInstance();
        Duration ttl = Duration.ofMillis(request.getTokenTtl());
        if (ttl.isZero() || ttl.isNegative())
            return TokenProvider.defaultTokenTimeToLive();
        //Override the default:
        cal.add(Calendar.MILLISECOND, Long.valueOf(ttl.toMillis()).intValue());
        return cal;
    }

    @Override
    public Response execute(Response message) throws RuntimeException {
        if (message != null && message.getStatus() != 200)
            return message;
        if (!(getMessage() instanceof LoginRequest))
            return new Response().setStatus(500).setError("Internal Server Error!");
        if (repository == null)
            throw new RuntimeException("UserRepository must not be null!");
        //Do the login:
        LoginRequest request = (LoginRequest) getMessage();
        Optional<User> exist = repository.findByUsername(request.getUsername());
        if (!exist.isPresent()) {
            exist = repository.findByCid(request.getUsername());
        }
//        if (!exist.isPresent()) {
//            exist = repository.findByEmail(request.getUsername());
//        }
        if (exist.isPresent()) {

            if (exist.get().getStatus() == 'I')
                return new Response().setStatus(401).setMessage("Failed to login: You are locked.");

            //PasswordEncoder::matches(RawPassword, EncodedPassword) == will return true/false
            if (!isNDILogin) {
                if (!encoder.matches(request.getPassword(), exist.get().getPassword()))
                    return new Response().setStatus(401).setMessage("Password didn't matched.");
            }

            //Now existing password matched: lets create the JWT token;
            try {
                //We create a token for 1 Hour:
                Calendar timeToLive = getTimeToLive();
                String tokenKey = getToken(exist.get(), timeToLive);
                Map<String, Object> data = new HashMap<>();
                data.put("accessToken", tokenKey);
                data.put("X-Auth-Token", tokenKey);

//                data.put("userId", exist.get().getId());
                data.put("userId", exist.get().getUserId());
                data.put("roles", exist.get().getAuthorities());

                //todo: need to check without data in sa_screen
                Set<GrantedAuthority> accessPermissions = new HashSet<>();
                List<ScreenDto> accessScreens = new ArrayList<>();
                for (Role saRole : exist.get().getRoles()) {
                    BigInteger roleId = saRole.getId();
                    List<PermissionListDto> permissionListDtos = roleWiseAccessPermissionService.getRoleMappedScreens(roleId);
                    if (permissionListDtos != null) {
                        for (PermissionListDto permissionListDto : permissionListDtos) {
                            Integer screenId = permissionListDto.getScreen_id();
                            ScreenDto screenDto = roleWiseAccessPermissionService.getAccessScreen(screenId);
                            screenDto.setView(false);
                            screenDto.setDelete(false);
                            screenDto.setEdit(false);
                            screenDto.setSave(false);
                            if (permissionListDto.getSave_allowed() != null && permissionListDto.getSave_allowed() == 'Y') {
                                accessPermissions.add(new SimpleGrantedAuthority(screenId + "-" + Permission.ADD));
                                screenDto.setSave(true);
                            }
                            if (permissionListDto.getEdit_allowed() != null && permissionListDto.getEdit_allowed() == 'Y') {
                                accessPermissions.add(new SimpleGrantedAuthority(screenId + "-" + Permission.EDIT));
                                screenDto.setEdit(true);
                            }
                            if (permissionListDto.getDelete_allowed() != null && permissionListDto.getDelete_allowed() == 'Y') {
                                accessPermissions.add(new SimpleGrantedAuthority(screenId + "-" + Permission.DELETE));
                                screenDto.setDelete(true);
                            }
                            //Screen permissions
                            if (permissionListDto.getView_allowed() != null && permissionListDto.getView_allowed() == 'Y') {
                                accessPermissions.add(new SimpleGrantedAuthority(screenId + "-" + Permission.VIEW));
                                screenDto.setView(true);
                                accessScreens.add(screenDto);
                                //accessScreens.add(tempScreenDto);
                            }

                        }
                    }
                }
                data.put("accessPermissions", accessPermissions);
                data.put("accessScreens", accessScreens);
                return new Response().setStatus(200).setMessage(Message.marshal(data));
            } catch (IOException e) {
                return new Response().setStatus(500).setMessage(e.getMessage());
            }
        } else {
            return new Response().setStatus(404).setMessage("User doesn't exist: " + request.getUsername());
        }
    }

    @Override
    public Response abort(Response message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
