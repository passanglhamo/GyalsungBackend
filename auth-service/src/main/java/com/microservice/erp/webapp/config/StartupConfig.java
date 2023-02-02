package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartupConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private IRoleRepository roleRepository;
    private IPolicyRepository policyRepository;
    private ScreenGroupRepository screenGroupRepository;
    private ScreenRepository screenRepository;
    private RoleWiseAccessPermissionRepository roleWiseAccessPermissionRepository;

    @Value("${app.god.user.username}")
    private String username;

    @Value("${app.god.user.password}")
    private String password;

    @Value("${app.god.user.mobile}")
    private String mobile;

    @Value("${app.god.user.role}")
    private String role;

    @Value("${app.god.user.policy}")
    private String policyName;

    @Value("${app.god.user.policyType}")
    private String policyType;

    @Value("${app.screen.policy.screenUrl}")
    private String policyUrl;

    @Value("${app.god.user.action}")
    private String action;

    @Value("${app.god.user.resource}")
    private String resource;

    @Value("${app.screen.group.name}")
    private String screenGroupName;

    @Value("${app.screen.groupName}")
    private String groupName;

    @Value("${app.screen.groupUrl}")
    private String groupUrl;

    @Value("${app.screen.name}")
    private String screenName;

    @Value("${app.screen.screenUrl}")
    private String url;

    @Value("${app.screen.role.name}")
    private String roleName;

    @Value("${app.screen.role.screenUrl}")
    private String roleUrl;

    @Value("${app.screen.roleWisePermission.name}")
    private String roleWisePermission;

    @Value("${app.screen.roleWisePermission.screenUrl}")
    private String roleWisePermissionUrl;



    public StartupConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository,
                         IPolicyRepository policyRepository, ScreenGroupRepository screenGroupRepository,
                         ScreenRepository screenRepository, RoleWiseAccessPermissionRepository roleWiseAccessPermissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.policyRepository = policyRepository;
        this.screenGroupRepository = screenGroupRepository;
        this.screenRepository = screenRepository;
        this.roleWiseAccessPermissionRepository = roleWiseAccessPermissionRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        //How to use Run:
        createGodUser(userRepository
                , username
                , password
                , role
                , policyName
                , Action.valueOf(action)
                , resource
                , policyType
                , screenGroupName
                , groupName
                , groupUrl
                , screenName
                , url
                , roleName
                , roleUrl
                , roleWisePermission
                , roleWisePermissionUrl
                , policyUrl
                );
    }

    private void createGodUser(UserRepository userRepository
            , String username, String password, String userRole
            , String policyName, Action action, String resource,
                               String policyType, String screenGroupName,
                               String groupName, String groupUrl,
                               String screenName, String url,
                               String roleName, String roleUrl,
                               String roleWisePermission, String roleWisePermissionUrl,
                               String policyUrl) {

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) return;

        ScreenGroup screenGroup = new ScreenGroup();
        if (!screenGroupRepository.findByScreenGroupName(screenGroupName).isPresent()) {
            screenGroup.setScreenGroupName(screenGroupName);
            screenGroup.setScreenGroupIconName(screenGroupName);
            screenGroupRepository.save(screenGroup);
        }

        if (!screenRepository.findByScreenName(groupName).isPresent()) {
            Screen screen = new Screen();
            screen.setScreenId(1);
            screen.setScreenName(groupName);
            screen.setScreenUrl(groupUrl);
            screen.setScreenIconName(groupName);
            screen.setScreenGroupId(screenGroupRepository.findByScreenGroupName(screenGroupName).get().getId());
            screenRepository.save(screen);

            Screen screen2 = new Screen();
            screen2.setScreenId(2);
            screen2.setScreenName(screenName);
            screen2.setScreenUrl(url);
            screen2.setScreenIconName(screenName);
            screen2.setScreenGroupId(screenGroupRepository.findByScreenGroupName(screenGroupName).get().getId());
            screenRepository.save(screen2);

            Screen screen3 = new Screen();
            screen3.setScreenId(3);
            screen3.setScreenName(roleName);
            screen3.setScreenUrl(roleUrl);
            screen3.setScreenIconName(roleName);
            screen3.setScreenGroupId(screenGroupRepository.findByScreenGroupName(screenGroupName).get().getId());
            screenRepository.save(screen3);

            Screen screen4 = new Screen();
            screen4.setScreenId(4);
            screen4.setScreenName(roleWisePermission);
            screen4.setScreenUrl(roleWisePermissionUrl);
            screen4.setScreenIconName(roleWisePermission);
            screen4.setScreenGroupId(screenGroupRepository.findByScreenGroupName(screenGroupName).get().getId());
            screenRepository.save(screen4);

            Screen screen5 = new Screen();
            screen5.setScreenId(5);
            screen5.setScreenName(policyName);
            screen5.setScreenUrl(policyUrl);
            screen5.setScreenIconName(policyName);
            screen5.setScreenGroupId(screenGroupRepository.findByScreenGroupName(screenGroupName).get().getId());
            screenRepository.save(screen4);
        }

        Policy policy = new Policy();
        if (policyRepository.findAll().size() == 0) {
            policy.setPolicyName(policyName);
            policy.setType(policyType);
            Statement statement = new Statement();
            statement.setResource(resource);
            statement.setAction(action);
            policy.addStatements(statement);
            policy.setCreatedBy(new Username(username));
            policyRepository.save(policy);
        }

        Role role = new Role();
        if (roleRepository.findAll().size() == 0) {
            role.setRoleName(userRole);
            role.setIsOpenUser('N');
            roleRepository.save(role);
        }

        Screen screenGroupDb = screenRepository.findByScreenName(groupName).get();
        Role existingRole = roleRepository.findByRoleName(userRole).get();

        if (!roleWiseAccessPermissionRepository.findById(screenGroupDb.getId()).isPresent()) {
            RoleWiseAccessPermission permission = new RoleWiseAccessPermission();
            permission.setScreenId(screenGroupDb.getScreenId());
            permission.setRoleId(existingRole.getId());
            permission.setViewAllowed('Y');
            permission.setSaveAllowed('Y');
            permission.setEditAllowed('Y');
            permission.setDeleteAllowed('Y');
            roleWiseAccessPermissionRepository.save(permission);
        }

        Screen screenDb = screenRepository.findByScreenName(screenName).get();
        if (!roleWiseAccessPermissionRepository.findById(screenDb.getId()).isPresent()) {
            RoleWiseAccessPermission permission = new RoleWiseAccessPermission();
            permission.setScreenId(screenDb.getScreenId());
            permission.setRoleId(existingRole.getId());
            permission.setViewAllowed('Y');
            permission.setSaveAllowed('Y');
            permission.setEditAllowed('Y');
            permission.setDeleteAllowed('Y');
            roleWiseAccessPermissionRepository.save(permission);
        }

        Screen screenRoleDb = screenRepository.findByScreenName(roleName).get();
        if (!roleWiseAccessPermissionRepository.findById(screenRoleDb.getId()).isPresent()) {
            RoleWiseAccessPermission permission = new RoleWiseAccessPermission();
            permission.setScreenId(screenRoleDb.getScreenId());
            permission.setRoleId(existingRole.getId());
            permission.setViewAllowed('Y');
            permission.setSaveAllowed('Y');
            permission.setEditAllowed('Y');
            permission.setDeleteAllowed('Y');
            roleWiseAccessPermissionRepository.save(permission);
        }

        Screen screenRoleWisePerDb = screenRepository.findByScreenName(roleWisePermission).get();
        if (!roleWiseAccessPermissionRepository.findById(screenRoleWisePerDb.getId()).isPresent()) {
            RoleWiseAccessPermission permission = new RoleWiseAccessPermission();
            permission.setScreenId(screenRoleWisePerDb.getScreenId());
            permission.setRoleId(existingRole.getId());
            permission.setViewAllowed('Y');
            permission.setSaveAllowed('Y');
            permission.setEditAllowed('Y');
            permission.setDeleteAllowed('Y');
            roleWiseAccessPermissionRepository.save(permission);
        }

        Policy existingPolicy = policyRepository.findByPolicyName(policyName).get();
        existingRole.addPolicies(existingPolicy);
        roleRepository.save(existingRole);
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(username);
        user.setEnabled(true);
        user.setSecrets(User.createRandomMapOfSecret());
        if (userRole != null && !userRole.isEmpty()) {
            Optional<Role> roleDb = roleRepository.findByRoleName(userRole);
            if (roleDb.isPresent()) {
                role = roleDb.get();
            } else {
                role.setRoleName(userRole);
            }
            user.addRoles(role);
        }
        userRepository.save(user);
    }


}
