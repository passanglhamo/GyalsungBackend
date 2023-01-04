package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.AccessPermission;
import com.microservice.erp.domain.repositories.PolicyRepository;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.am.*;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.models.SearchQuery;
import com.microservice.erp.webapp.config.SecurityConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/access/v1")
public class AccessController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PolicyRepository policyRepository;

    public AccessController(UserRepository userRepository
    , RoleRepository roleRepository
    , PolicyRepository policyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.policyRepository = policyRepository;
    }

    @PostMapping("/hasPermission")
    public ResponseEntity<AccessPermission> hasPermission(@RequestBody AccessPermission permission){
        AccessPermission response = (AccessPermission) new AccessPermission()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage("Not Authorized.");
        //
        if (permission.getUsername() == null
                || permission.getUsername().isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        if (permission.getStatement() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        if (permission.getStatement().getResource() == null
                || permission.getStatement().getResource().isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        //
        HasAccessPermission hasPermission = new HasAccessPermission(userRepository, permission);
        permission = hasPermission.execute(null);
        return (permission.getStatus() == 200)
                ? ResponseEntity.ok(permission)
                : ResponseEntity.status(permission.getStatus()).body(permission);
    }

    @PostMapping("/add/new/role/{roleName}")
    public ResponseEntity<String> addNewRole(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("roleName") String roleName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Role role = new Role();
        role.setRoleName(roleName);
        SaveRole saveRole = new SaveRole(roleRepository, role);
        Response response = saveRole.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/add/new/policy/{policyName}")
    public ResponseEntity<String> addNewPolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("policyName") String policyName
            , @RequestParam(required = false) String policyType) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Policy policy = new Policy();
        policy.setServiceName(policyName);
        policy.setType(policyType);
        SavePolicy savePolicy = new SavePolicy(policyRepository, policy);
        Response response = savePolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/add/policy/to/user/{username}/{policyName}")
    public ResponseEntity<String> addPolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
                                            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
                                            , @PathVariable("username") String username
                                            , @PathVariable("policyName") String policyName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        User user = new User();
        user.setUsername(username);
        Policy policy = new Policy();
        policy.setServiceName(policyName);
        AssignPolicyToUser assignPolicy = new AssignPolicyToUser(userRepository, policyRepository, user, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/remove/policy/from/user/{username}/{serviceName}")
    public ResponseEntity<String> removePolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("username") String username
            , @PathVariable("serviceName") String serviceName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        User user = new User();
        user.setUsername(username);
        Optional<Policy> opt = policyRepository.findByServiceName(serviceName);
        Policy policy = opt.isPresent() ? opt.get() : new Policy();
        //policy.setServiceName(serviceName);
        RemovePolicyFromUser removePolicy = new RemovePolicyFromUser(userRepository, user, policy);
        Response response = removePolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/add/role/to/user/{username}/{roleName}")
    public ResponseEntity<String> addRole(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("username") String username
            , @PathVariable("roleName") String roleName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        User user = new User();
        user.setUsername(username);
        Role role = new Role();
        role.setRoleName(roleName);
        AssignRoleToUser assignRole = new AssignRoleToUser(userRepository, roleRepository, user, role);
        Response response = assignRole.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/remove/role/from/user/{username}/{roleName}")
    public ResponseEntity<String> removeRole(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("username") String username
            , @PathVariable("roleName") String roleName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        User user = new User();
        user.setUsername(username);
        Optional<Role> opt = roleRepository.findRoleByRoleName(roleName);
        Role role = opt.isPresent() ? opt.get() : new Role();
        //role.setName(roleName);
        RemoveRoleFromUser removeRole = new RemoveRoleFromUser(userRepository, user, role);
        Response response = removeRole.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/add/policy/to/role/{roleName}/{policyName}")
    public ResponseEntity<String> addPolicyToRole(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("roleName") String roleName
            , @PathVariable("policyName") String policyName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Role role = new Role();
        role.setRoleName(roleName);
        Policy policy = new Policy();
        policy.setServiceName(policyName);
        AssignPolicyToRole assignPolicy = new AssignPolicyToRole(roleRepository, policyRepository, role, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/remove/policy/from/role/{roleName}/{serviceName}")
    public ResponseEntity<String> removePolicyFromRole(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("roleName") String roleName
            , @PathVariable("serviceName") String serviceName) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Role role = new Role();
        role.setRoleName(roleName);
        Optional<Policy> policyOpt = policyRepository.findByServiceName(serviceName);
        Policy policy = (policyOpt.isPresent()) ? policyOpt.get() : new Policy();
        //policy.setServiceName(serviceName);
        //
        RemovePolicyFromRole assignPolicy = new RemovePolicyFromRole(roleRepository, role, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/add/statement/to/policy/{serviceName}")
    public ResponseEntity<String> addStatementToPolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("serviceName") String serviceName
            , @RequestBody Statement statement) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Policy policy = new Policy();
        policy.setServiceName(serviceName);
        AddStatementToPolicy addStmt = new AddStatementToPolicy(policyRepository, policy, statement);
        Response response = addStmt.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/remove/statement/to/policy/{serviceName}")
    public ResponseEntity<String> removeStatementFromPolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @PathVariable("serviceName") String serviceName
            , @RequestBody Statement statement) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
        }
        Policy policy = new Policy();
        policy.setServiceName(serviceName);
        RemoveStatementFromPolicy addStmt = new RemoveStatementFromPolicy(policyRepository, policy, statement);
        Response response = addStmt.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @PostMapping("/fetch/users")
    public ResponseEntity<List<User>> fetchAllUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @RequestBody(required = false) SearchQuery query) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        Integer page = query.getPage();
        Integer limit = query.getSize();
        if (limit <= 0) limit = 10;
        if (page <= 0) page = 0;

        Page<User> res = userRepository.findAll(PageRequest.of(page, limit));
        Response response = new Response().setStatus(res.getSize() > 0 ? 200 : 500);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(res.getContent())
                : ResponseEntity.status(response.getStatus()).body(new ArrayList<>());
    }

    @PostMapping("/fetch/roles")
    public ResponseEntity<List<Role>> fetchAllRoles(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @RequestBody(required = false) SearchQuery query) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        Integer page = query.getPage();
        Integer limit = query.getSize();
        if (limit <= 0) limit = 10;
        if (page <= 0) page = 0;

        Page<Role> res = roleRepository.findAll(PageRequest.of(page, limit));
        Response response = new Response().setStatus(res.getSize() > 0 ? 200 : 500);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(res.getContent())
                : ResponseEntity.status(response.getStatus()).body(new ArrayList<>());
    }

    @PostMapping("/fetch/policies")
    public ResponseEntity<List<Policy>> fetchAllPolicy(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
            , @ApiIgnore @AuthenticationPrincipal UserDetails principal
            , @RequestBody(required = false) SearchQuery query) {
        //
        if (!SecurityConfig.matchAnyAdminRole(principal.getAuthorities())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        Integer page = query.getPage();
        Integer limit = query.getSize();
        if (limit <= 0) limit = 10;
        if (page <= 0) page = 0;

        Page<Policy> res = policyRepository.findAll(PageRequest.of(page, limit));
        Response response = new Response().setStatus(res.getSize() > 0 ? 200 : 500);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(res.getContent())
                : ResponseEntity.status(response.getStatus()).body(new ArrayList<>());
    }

}
