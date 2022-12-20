package com.microservice.erp.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupConfig implements CommandLineRunner {

    //private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

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

    @Value("${app.god.user.action}")
    private String action;

    @Value("${app.god.user.resource}")
    private String resource;

    public StartupConfig(PasswordEncoder passwordEncoder) {
        //this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //How to use Run:
//        createGodUser(userRepository
//                , username
//                , password
//                , mobile
//                , role
//                , policyName
//                , Action.valueOf(action)
//                , resource);
    }

//    private void createGodUser(UserRepository userRepository
//            , String username, String password, String mobile, String userRole
//            , String policyName, Action action, String resource) {
//
//        Optional<User> opt = userRepository.findByUsername(username);
//        if (opt.isPresent()) return;
//        //
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setEmail(username);
//        user.setMobile(mobile);
//        user.setEnabled(true);
//        user.setSecrets(User.createRandomMapOfSecret());
//        //
//        Role role = new Role();
//        role.setName(userRole);
//        user.addRoles(role);
//        //
//        Statement statement = new Statement();
//        statement.setAction(action);
//        statement.setResource(resource);
//        //
//        Policy policy = new Policy();
//        policy.setServiceName(policyName);
//        policy.addStatements(statement);
//        role.addPolicies(policy);
//        //
//        userRepository.save(user);
//    }


}
