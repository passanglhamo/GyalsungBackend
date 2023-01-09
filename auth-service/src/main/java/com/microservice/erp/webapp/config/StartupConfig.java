package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
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
                , mobile
                , role
                , policyName
                , Action.valueOf(action)
                , resource);
    }

    private void createGodUser(UserRepository userRepository
            , String username, String password, String mobile, String userRole
            , String policyName, Action action, String resource) {

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) return;

        Role role = new Role();
        if (roleRepository.findAll().size() == 0) {
            role.setRoleName(userRole);
            role.setIsOpenUser('N');
            roleRepository.save(role);
        }

//        Policy policy = new Policy();
//        if (policyRepository.findAll().size() == 0) {
//            Statement statement = new Statement();
//            statement.setAction(action);
//            statement.setResource(resource);
//            policy.setPolicyName(policyName);
//            policy.addStatements(statement);
//            policyRepository.save(policy);
//        }


        //
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(username);
        //user.setMobile(mobile);
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

//        Set<Role> saRoles = new HashSet<>();
//        Role saRoleDb = roleRepository.findRoleByRoleName(userRole).get();// to get student user role information
//        saRoles.add(saRoleDb);
//        user.setRoles(saRoles);
        //
//        Role role = new Role();
//        role.setRoleName(userRole);
//        user.addRoles(role);
        //
//        Optional<Policy> policyDb = policyRepository.findByPolicyName(policyName);
//        role.addPolicies(policyDb.get());

        //
//        policy.setPolicyName(policyName);
//        policy.addStatements(statement);
//        role.addPolicies(policy);
        //
        userRepository.save(user);
    }


}
