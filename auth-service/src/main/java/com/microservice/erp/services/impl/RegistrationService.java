package com.microservice.erp.services.impl;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.NewAccountRequest;
import com.microservice.erp.domain.models.NewTenantRequest;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.CheckUserExist;
import com.microservice.erp.domain.tasks.iam.CreateNewTenant;
import com.microservice.erp.domain.tasks.iam.CreateNewUser;
import com.microservice.erp.domain.tasks.sender.SendEmail;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.services.definition.iRegistration;
import com.it.soul.lab.sql.query.models.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RegistrationService implements iRegistration {

    private static Logger LOG = LoggerFactory.getLogger(RegistrationService.class.getSimpleName());
    private PasswordEncoder passwordEncoder;
    private UserRepository repository;
    private IRoleRepository roleRepository;
    private RestTemplate mailTemplate;

    @Value("${app.email.noreply}")
    private String noReply;

    public RegistrationService(UserRepository repository
            , IRoleRepository roleRepository
            , PasswordEncoder passwordEncoder
            , @Qualifier("mailTemplate") RestTemplate mailTemplate) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailTemplate = mailTemplate;
    }

    @Value("${app.template.email.welcome}")
    private String welcomeTemplate;

    @Value("${app.login.href}")
    private String loginRef;

    @Override
    public Response createNew(NewAccountRequest account) {
        final Response response = new Response()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setMessage("Under Construction!");

        Row properties = new Row()
                .add("name", account.getUsername())
                .add("regDate", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
                .add("defaultPass", account.getPassword())
                .add("loginRef", loginRef);

        SendEmail emailTask = new SendEmail(mailTemplate
                , noReply
                , account.getEmail()
                , String.format("Greetings %s !", account.getUsername())
                , welcomeTemplate
                , properties);

        TaskStack stack = TaskStack.createSync(true);
        stack.push(new CheckUserExist(repository, account));
        stack.push(new CreateNewUser(passwordEncoder, repository, roleRepository, account));
        stack.push(emailTask);
        stack.commit(true, (message, state) -> {
            LOG.info("Account Creating Status: " + state.name());
            if (message != null){
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
            }
        });
        return response;
    }

    @Override
    public Response isAccountExist(String username) {
        return new CheckUserExist(repository, username).execute(null);
    }

    @Override
    public Response createNewTenant(NewTenantRequest tenant) {
        final Response response = new Response()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setMessage("Under Construction!");
        TaskStack stack = TaskStack.createSync(true);
        stack.push(new CheckUserExist(repository, tenant.getEmail()));
        stack.push(new CreateNewTenant(passwordEncoder, null, tenant));
        stack.push(new CreateNewUser(passwordEncoder, repository, roleRepository));
        stack.commit(true, (message, state) -> {
            LOG.info("Tenant Creating Status: " + state.name());
            if (message != null){
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
            }
        });
        return response;
    }

    @Override
    public ResponseEntity<?> userByUserId(BigInteger userId) {
        return ResponseEntity.ok(repository.findByUserId(userId).get());
    }
}
