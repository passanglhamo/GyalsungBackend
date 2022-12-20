//package com.microservice.erp.domain.tasks.iam;
//
//import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
//import com.microservice.erp.domain.entities.Role;
//import com.microservice.erp.domain.entities.User;
//import com.microservice.erp.domain.models.NewAccountRequest;
//import com.microservice.erp.domain.repositories.RoleRepository;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.rest.models.Response;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//public class CreateNewUser extends AbstractTask<NewAccountRequest, Response> {
//
//    private PasswordEncoder encoder;
//    private UserRepository repository;
//    private RoleRepository roleRepository;
//
//    public CreateNewUser(PasswordEncoder encoder, UserRepository repository) {
//        this.encoder = encoder;
//        this.repository = repository;
//    }
//
//    public CreateNewUser(PasswordEncoder encoder, UserRepository repository, RoleRepository roleRepository) {
//        this(encoder, repository);
//        this.roleRepository = roleRepository;
//    }
//
//    public CreateNewUser(PasswordEncoder encoder, UserRepository repository, NewAccountRequest request) {
//        super(request);
//        this.encoder = encoder;
//        this.repository = repository;
//    }
//
//    public CreateNewUser(PasswordEncoder encoder, UserRepository repository, RoleRepository roleRepository, NewAccountRequest request) {
//        this(encoder, repository, request);
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public Response execute(NewAccountRequest message) throws RuntimeException {
//        if (message != null && message.getStatus() != 404) {
//            return message.setStatus(400);
//        }
//        //Do Create New User Account using username and password.
//        NewAccountRequest account = (message == null) ? getMessage() : message;
//        User user = new User();
//        user.unmarshallingFromMap(account.marshallingToMap(true),true);
//        user.setPassword(encoder.encode(account.getPassword()));
//        user.setSecrets(User.createRandomMapOfSecret());
//        if (roleRepository != null) {
//            if (account.getRole() != null && !account.getRole().isEmpty()){
//                Role role = new Role();
//                Optional<Role> opt = roleRepository.findRoleByName(account.getRole());
//                if (opt.isPresent()){
//                    role = opt.get();
//                }else {
//                    role.setName(account.getRole());
//                }
//                user.addRoles(role);
//            }
//        }
//        //
//        if (repository != null){
//            User nUser = repository.save(user);
//            if (nUser != null)
//                return new Response().setStatus(200)
//                    .setMessage("Account create with username: " + account.getUsername());
//            else
//                return new Response().setStatus(500)
//                        .setMessage("Account creation has been failed for username: " + account.getUsername());
//        }
//        throw new RuntimeException("UserRepository Must Not be Null!");
//    }
//
//    @Override
//    public Response abort(NewAccountRequest message) throws RuntimeException {
//        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//
//}
