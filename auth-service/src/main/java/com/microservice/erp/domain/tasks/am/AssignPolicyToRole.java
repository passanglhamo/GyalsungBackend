//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
//import com.microservice.erp.domain.entities.Policy;
//import com.microservice.erp.domain.entities.Role;
//import com.microservice.erp.domain.repositories.RoleRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//
//import java.util.Map;
//import java.util.Optional;
//
//public class AssignPolicyToRole extends AbstractTask<Message, Response> {
//
//    private RoleRepository repository;
//    private Role role;
//    private Policy policy;
//
//    public AssignPolicyToRole(RoleRepository repository, Role role, Policy policy) {
//        super(new Property("role", role.marshallingToMap(true))
//                , new Property("policy", policy.marshallingToMap(true)));
//        this.repository = repository;
//        this.role = role;
//        this.policy = policy;
//    }
//
//    @Override
//    public Response execute(Message message) throws RuntimeException {
//        if (policy == null){
//            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
//            policy = new Policy();
//            policy.unmarshallingFromMap(savedData, true);
//        }
//        if (role == null){
//            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("role");
//            role = new Role();
//            role.unmarshallingFromMap(savedData, true);
//        }
//        if (repository != null){
//            Optional<Role> exist = repository.findRoleByName(role.getName());
//            if (exist.isPresent()){
//                role = exist.get();
//                role.addPolicies(policy);
//                Role saved = repository.save(role);
//                return new Response().setMessage("Role successfully Updated with Policy: " + saved.getId()).setStatus(200);
//            }
//            return new Response().setMessage("Role Not Found.").setStatus(500);
//        }
//        return new Response().setMessage("RoleRepository is null.").setStatus(500);
//    }
//
//    @Override
//    public Response abort(Message message) throws RuntimeException {
//        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//}
