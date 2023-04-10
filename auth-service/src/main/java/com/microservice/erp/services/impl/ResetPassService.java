package com.microservice.erp.services.impl;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.ChangePassword;
import com.microservice.erp.domain.tasks.iam.CheckUserExist;
import com.microservice.erp.domain.tasks.iam.ForgotPassword;
import com.microservice.erp.domain.tasks.sender.SendEmail;
import com.microservice.erp.domain.tasks.sender.SendForgetPasswordEmail;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.services.definition.iResetPassword;
import com.it.soul.lab.sql.query.models.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Optional;

@Service
public class ResetPassService implements iResetPassword {

    private static Logger LOG = LoggerFactory.getLogger(RegistrationService.class.getSimpleName());
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private RestTemplate notifyTemplate;
    private RestTemplate mailTemplate;

    @Value("${app.email.noreply}")
    private String noReply;

    public ResetPassService(UserRepository repository
            , PasswordEncoder passwordEncoder
            , @Qualifier("notifyTemplate") RestTemplate notifyTemplate
            , @Qualifier("mailTemplate") RestTemplate mailTemplate) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.notifyTemplate = notifyTemplate;
        this.mailTemplate = mailTemplate;
    }

    @Value("${app.template.email.forget.password}")
    private String forgetPassTemplate;

    @Value("${app.template.email.reset.password.successful}")
    private String resetPassSuccessTemplate;

    @Value("${app.template.email.reset.password.failed}")
    private String resetPassFailedTemplate;

    @Value("${app.reset.password.href}")
    private String resetPassRef;

    @Value("${app.login.href}")
    private String loginRef;

    @Value("${app.forget.password.token.ttl.duration.millis}")
    private long tokenTtl;

    @Override
    public Response didForget(String username) {
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        /*Optional<User> optUser = repository.findByUsername(username);
        String email = optUser.isPresent() ? optUser.get().getEmail() : "ept@g.com";*/
        Optional<User> optUser = repository.findByUsername(username);
        if (!optUser.isPresent()) return Response.CreateErrorResponse(new Exception(username + " not found!"));
        String email = optUser.get().getEmail();
        if (email == null || email.isEmpty()) return Response.CreateErrorResponse(new Exception(username + " doesn't have email address."));
        //
        TaskStack forgetPassStack = TaskStack.createSync(true);
        forgetPassStack.push(new CheckUserExist(repository, username));
        forgetPassStack.push(new ForgotPassword(repository, username, Duration.ofMillis(tokenTtl)));
        forgetPassStack.push(new SendForgetPasswordEmail(mailTemplate
                , noReply
                , email
                , "Forget Password!"
                , forgetPassTemplate
                , new Row()
                .add("name", username)
                .add("resetRef", resetPassRef)));
        forgetPassStack.commit(true, (message, state) -> {
            LOG.info("ForgetPassword Status: " + state.name());
            if (message != null){
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
            }
        });
        //
        return response;
    }

    @Override
    public Response doReset(String token, String oldPass, String newPass) {
        JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
        String username = payload.getIss();
        String email = payload.getData().get("email");
        //Change-Password-Task:
        ChangePassword changePassword = new ChangePassword(token, oldPass, newPass, repository, passwordEncoder);
        Response response = changePassword.execute(null);
        //Send-Email-Task:
        String templateName = (response.getStatus() == HttpStatus.OK.value())
                ? resetPassSuccessTemplate
                : resetPassFailedTemplate;
        SendEmail sendEmail = new SendEmail(mailTemplate
                , noReply
                , email
                , "Password Reset Successful"
                , templateName
                , new Row()
                .add("name", username)
                .add("loginRef", loginRef));
        sendEmail.execute(response);
        //
        return response;
    }
}
