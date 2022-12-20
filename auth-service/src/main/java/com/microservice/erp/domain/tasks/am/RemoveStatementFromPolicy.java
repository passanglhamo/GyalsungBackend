//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
//import com.microservice.erp.domain.entities.Policy;
//import com.microservice.erp.domain.entities.Statement;
//import com.microservice.erp.domain.repositories.PolicyRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class RemoveStatementFromPolicy extends AbstractTask<Message, Response> {
//
//    private PolicyRepository repository;
//    private Policy policy;
//    private Statement[] statements;
//
//    public RemoveStatementFromPolicy(PolicyRepository repository, Policy policy, Statement...statements) {
//        super(new Property("policy", policy.marshallingToMap(true)));
//        this.repository = repository;
//        this.policy = policy;
//        this.statements = statements;
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
//            if (exist.isPresent()){
//                policy = exist.get();
//                List<String> resources = Stream.of(statements)
//                        .map(stmt -> stmt.getResource())
//                        .collect(Collectors.toList());
//                List<Statement> toBeRemoved = policy.getStatements()
//                        .stream()
//                        .filter(stmt -> resources.contains(stmt.getResource()))
//                        .collect(Collectors.toList());
//                policy.getStatements().removeAll(toBeRemoved);
//                Policy saved = repository.save(policy);
//                return new Response().setMessage("Policy successfully Updated with Statements: " + saved.getId()).setStatus(200);
//            }
//            return new Response().setMessage("Policy Not Found.").setStatus(500);
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
