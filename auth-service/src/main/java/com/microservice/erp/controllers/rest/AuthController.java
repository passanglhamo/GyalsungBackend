package com.microservice.erp.controllers.rest;

import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.data.base.DataSource;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.*;
import com.microservice.erp.services.definition.iLogin;
import com.microservice.erp.services.definition.iRegistration;
import com.microservice.erp.services.definition.iResetPassword;
import com.microservice.erp.webapp.config.SecurityConfig;
import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.DCRC_CitizenDetailsAPI.DefaultApi;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizenDetailsResponse;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private static Logger LOG = LoggerFactory.getLogger(AuthController.class.getSimpleName());
    private iLogin login;
    private iRegistration registration;
    private iResetPassword resetPassword;
    private DataSource<String, LoginRetryCount> cache;

    @Value("${app.login.retry.count}")
    private int loginMaxRetryCount;

    @Value("${app.login.retry.block.duration.millis}")
    private long blockDurationInMillis;

    public AuthController(iLogin login
            , iRegistration registration
            , iResetPassword resetPassword
            , @Qualifier("loginRetryCount") DataSource<String, LoginRetryCount> cache) {
        this.login = login;
        this.registration = registration;
        this.resetPassword = resetPassword;
        this.cache = cache;
    }

    @GetMapping("/isAccountExist")
    public ResponseEntity<String> isExist(@RequestParam("username") String username) {
        Response response = registration.isAccountExist(username);
        if (response.getStatus() == HttpStatus.OK.value())
            return ResponseEntity.ok(response.toString());
        else
            return ResponseEntity.status(response.getStatus()).body(response.toString());
    }

    @PostMapping("/new/user/account")
    public ResponseEntity<String> newUserAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @Valid @RequestBody NewAccountRequest account) {
        //
        //token = TokenValidator.parseToken(token, "Bearer ");
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        account.setRole("USER");
        Response response = registration.createNew(account);
        if (response.getStatus() == HttpStatus.OK.value())
            return ResponseEntity.ok(response.toString());
        else
            return ResponseEntity.status(response.getStatus()).body(response.toString());
    }

    @PostMapping("/new/admin/account")
    public ResponseEntity<String> newAdminAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @Valid @RequestBody NewAccountRequest account) {
        //
        //token = TokenValidator.parseToken(token, "Bearer ");
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        account.setRole("ADMIN");
        Response response = registration.createNew(account);
        if (response.getStatus() == HttpStatus.OK.value())
            return ResponseEntity.ok(response.toString());
        else
            return ResponseEntity.status(response.getStatus()).body(response.toString());
    }

    @PostMapping("/nonblock/login")
    public ResponseEntity<?> nonBlockingLogin(@Valid @RequestBody LoginRequest request) throws IOException {
        //
//        Response response = login.doLogin(request);
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            return ResponseEntity.ok(response.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
        return login.doLogin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        //Check login-retry-count: if exceed then refuse login action:
        LoginRetryCount count = cache.read(request.getUsername());
        if (count != null) {
            if (count.isMaxTryExceed()) {
                if (count.isTimePassed(blockDurationInMillis)) {
                    count.resetFailedCount();
                } else {
                    long timeRemain = (blockDurationInMillis - count.timeElapsed()) / 1000;
                    Response response = new Response()
                            .setStatus(HttpStatus.FORBIDDEN.value())
                            .setMessage("Please wait and try again " + timeRemain + " seconds later.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response.toString());
                }
            }
        }
        //
<<<<<<< HEAD
        // Response response = login.doLogin(request);
=======
//        Response response = login.doLogin(request);
>>>>>>> 77e5ae0cdff5b459d87394883ab563e4354a098e
        Response response = null;
        //If-Login Failed: then track-login-failed-count:
        if (response.getStatus() == HttpStatus.OK.value()) {
            if (count != null) cache.remove(request.getUsername());
            return ResponseEntity.ok(response.toString());
        } else {
            if (count == null) count = new LoginRetryCount(loginMaxRetryCount);
            count.incrementFailedCount();
            cache.put(request.getUsername(), count);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }

    @GetMapping("/forget")
    public ResponseEntity<String> forget(@RequestParam("username") String username) {
        //
        Response response = resetPassword.didForget(username);
        //Remove the message part from response when deploy in Production:-
        response.setMessage("");
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/forget/reset")
    public ResponseEntity<String> reset(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @RequestParam(name = "password") String password) {
        //
        token = TokenValidator.parseToken(token, "Bearer ");
        //Password Validation:
        ChangePassRequest.applyPasswordRules(password);
        //
        Response response = resetPassword.doReset(token
                , null, password);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = login.doLogout(token, principal);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @Valid @RequestBody ChangePassRequest changeRequest) {
        //
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = resetPassword.doReset(token
                , changeRequest.getOldPassword()
                , changeRequest.getNewPassword());
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }

    @GetMapping("/isValidToken")
    public ResponseEntity<String> isValid(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {

        LOG.info("isValidToken Called with token: {}; username:{}", token, principal.getUsername());

        //
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = login.isValidToken(token, principal);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {
        //
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = login.refreshToken(token, principal);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }

    @GetMapping("/userByUserId")
    public ResponseEntity<?> userByUserIds(@RequestParam("userId") BigInteger userId) {
        return registration.userByUserId(userId);
    }

    @PostMapping("/ndiCode")
    public ResponseEntity<?> ndiCode() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String requestString = "{\n" +
                " \"proofName\": \"TTPL\",\n" +
                " \"proofAttributes\": [\n" +
                " {\n" +
                " \"name\":\"ID Number\"," +
                " \"restrictions\":[]\n" +
                "}]\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Resource resource = new ClassPathResource("/ndiapi/ndiApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String verifyNdi = props.getProperty("verifyNDI.endPointURL");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestString, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                verifyNdi, HttpMethod.POST, requestEntity, String.class
        );


        return responseEntity;
    }
}
