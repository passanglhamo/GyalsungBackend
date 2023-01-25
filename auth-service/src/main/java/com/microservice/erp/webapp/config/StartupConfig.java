package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
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

    @Value("${app.god.user.action}")
    private String action;

    @Value("${app.god.user.resource}")
    private String resource;

    public StartupConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository,
                         IPolicyRepository policyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.policyRepository = policyRepository;

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
                , policyType);
    }

    private void createGodUser(UserRepository userRepository
            , String username, String password,  String userRole
            , String policyName, Action action, String resource,
                               String policyType) {

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) return;



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

        Role existingRole = roleRepository.findByRoleName(userRole).get();
        Policy existingPolicy = policyRepository.findByPolicyName(policyName).get();
        existingRole.addPolicies(existingPolicy);
        roleRepository.save(existingRole);
        //
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
