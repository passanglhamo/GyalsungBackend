//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
//import com.microservice.erp.domain.entities.Policy;
//import com.microservice.erp.domain.repositories.PolicyRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//
//import java.util.Map;
//import java.util.Optional;
//
//public class SavePolicy extends AbstractTask<Message, Response> {
//
//    private PolicyRepository repository;
//    private Policy policy;
//
//    public SavePolicy(PolicyRepository repository, Policy data) {
//        super(new Property("policy", data.marshallingToMap(true)));
//        this.repository = repository;
//        this.policy = data;
//    }
//
//    @Override
//    public Response execute(Message message) throws RuntimeException {
//        if (policy == null){
//            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
//            policy = new Policy();
//            policy.unmarshallingFromMap(savedData, true);
//        }
//        if (repository != null){
//            Optional<Policy> exist = repository.findByServiceName(policy.getServiceName());
//            if (!exist.isPresent()){
//                Policy saved = repository.save(policy);
//                return new Response().setMessage("Policy successfully created: " + saved.getId()).setStatus(200);
//            } else {
//                policy.setId(exist.get().getId());
//                Policy saved = repository.save(policy);
//                return new Response().setMessage("Policy successfully updated: " + saved.getId()).setStatus(200);
//            }
//        }
//        return new Response().setMessage("PolicyRepository is null.").setStatus(500);
//    }
//
//    @Override
//    public Response abort(Message message) throws RuntimeException {
//        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//}
