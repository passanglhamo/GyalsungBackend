package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.helper.Action;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Rajib Kumer Ghosh
 */

@Component
public class StartupConfig implements CommandLineRunner {

    //    @EventListener
//    public void handleContextStartedListener(ContextRefreshedEvent event){
//        System.out.println("ContextStarted....");
//    }
//
//    @EventListener
//    public void handleContextStoppedListener(ContextClosedEvent event){
//        System.out.println("ContextStopped....");
//    }
    private ISaUserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public StartupConfig(ISaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    private void createGodUser(ISaUserRepository userRepository
            , String username, String password, String mobile, String userRole
            , String policyName, Action action, String resource) {

        SaUser userInfo = userRepository.findByUsername(username);
        if (!Objects.isNull(userInfo)) return;

        SaUser user = new SaUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName("Admin");
        user.setEmail("Admin@gmail.com");
        user.setMobileNo(mobile);
        user.setSignupUser('A');
        user.setStatus('A');
        user.setEmail(username);

        user.setSecrets(SaUser.createRandomMapOfSecret());
        //
        SaRole role = new SaRole();
        role.setRoleName(userRole);
        role.setIsOpenUser('y');
        user.addRoles(role);
        //
//        SaStatement statement = new SaStatement();
//        statement.setAction(action);
//        statement.setResource(resource);
        //
//        SaPolicy policy = new SaPolicy();
//        policy.setServiceName(policyName);
//        policy.addStatements(statement);
//        role.addPolicies(policy);
        //
        userRepository.save(user);
    }


}
