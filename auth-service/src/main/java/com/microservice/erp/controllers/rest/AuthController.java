package com.microservice.erp.controllers.rest;

import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.data.base.DataSource;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.models.LoginRetryCount;
import com.microservice.erp.services.definition.iLogin;
import com.microservice.erp.services.definition.iResetPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private static Logger LOG = LoggerFactory.getLogger(AuthController.class.getSimpleName());
    private iLogin login;
    //    private iRegistration registration;
    // private iResetPassword resetPassword;
    private DataSource<String, LoginRetryCount> cache;

    @Value("${app.login.retry.count}")
    private int loginMaxRetryCount;

    @Value("${app.login.retry.block.duration.millis}")
    private long blockDurationInMillis;

    public AuthController(iLogin login
            , @Qualifier("loginRetryCount") DataSource<String, LoginRetryCount> cache) {
        this.login = login;
        this.cache = cache;
    }

//    @GetMapping("/isAccountExist")
//    public ResponseEntity<String> isExist(@RequestParam("username") String username) {
//        Response response = registration.isAccountExist(username);
//        if (response.getStatus() == HttpStatus.OK.value())
//            return ResponseEntity.ok(response.toString());
//        else
//            return ResponseEntity.status(response.getStatus()).body(response.toString());
//    }

    // Commented for
//    @PostMapping("/new/user/account")
//    public ResponseEntity<String> newUserAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
//            , @Valid @RequestBody NewAccountRequest account) {
//        //
//        //token = TokenValidator.parseToken(token, "Bearer ");
//        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
//        }
//        account.setRole("USER");
//        Response response = registration.createNew(account);
//        if (response.getStatus() == HttpStatus.OK.value())
//            return ResponseEntity.ok(response.toString());
//        else
//            return ResponseEntity.status(response.getStatus()).body(response.toString());
//    }

    // For future reference
//    @PostMapping("/new/admin/account")
//    public ResponseEntity<String> newAdminAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
//            , @Valid @RequestBody NewAccountRequest account) {
//        //
//        //token = TokenValidator.parseToken(token, "Bearer ");
//        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
//        }
//        account.setRole("ADMIN");
//        Response response = registration.createNew(account);
//        if (response.getStatus() == HttpStatus.OK.value())
//            return ResponseEntity.ok(response.toString());
//        else
//            return ResponseEntity.status(response.getStatus()).body(response.toString());
//    }

    @PostMapping("/nonblock/login")
    public ResponseEntity<?> nonBlockingLogin(@Valid @RequestBody LoginRequest request) throws IOException {
        return login.doLogin(request);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws IOException {
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
        return login.doLogin(request);
        //If-Login Failed: then track-login-failed-count:
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            if (count != null) cache.remove(request.getUsername());
//            return ResponseEntity.ok(response.toString());
//        } else {
//            if (count == null) count = new LoginRetryCount(loginMaxRetryCount);
//            count.incrementFailedCount();
//            cache.put(request.getUsername(), count);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
    }

//    @GetMapping("/forget")
//    public ResponseEntity<String> forget(@RequestParam("username") String username) {
//        //
//        Response response = resetPassword.didForget(username);
//        return ResponseEntity.ok(response.toString());
//    }

    // For future reference
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {
//        token = TokenValidator.parseToken(token, "Bearer ");
//        Response response = login.doLogout(token, principal);
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            return ResponseEntity.ok(response.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
//    }

//    @PostMapping("/reset")
//    public ResponseEntity<String> reset(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @Valid @RequestBody ChangePassRequest changeRequest) {
//        //
//        token = TokenValidator.parseToken(token, "Bearer ");
//        Response response = resetPassword.doReset(token
//                , changeRequest.getOldPassword()
//                , changeRequest.getNewPassword());
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            return ResponseEntity.ok(response.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
//    }

//    @GetMapping("/isValidToken")
//    public ResponseEntity<String> isValid(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @ApiIgnore @AuthenticationPrincipal UserDetails principal){
//        //
//        token = TokenValidator.parseToken(token, "Bearer ");
//        Response response = login.isValidToken(token, principal);
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            return ResponseEntity.ok(response.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
//    }

    @GetMapping("/isValidToken")
    public ResponseEntity<?> isValid(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {
        return login.isValidToken(token, principal);
    }

//    @GetMapping("/refreshToken")
//    public ResponseEntity<String> refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
//            , @ApiIgnore @AuthenticationPrincipal UserDetails principal) {
//        //
//        token = TokenValidator.parseToken(token, "Bearer ");
//        Response response = login.refreshToken(token, principal);
//        if (response.getStatus() == HttpStatus.OK.value()) {
//            return ResponseEntity.ok(response.toString());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
//        }
//    }

}