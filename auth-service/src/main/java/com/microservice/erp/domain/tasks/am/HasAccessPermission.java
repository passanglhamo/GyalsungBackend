package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.AccessPermission;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HasAccessPermission extends AbstractTask<Message, AccessPermission> {

    private UserRepository repository;

    public HasAccessPermission(UserRepository repository, AccessPermission permission) {
        super(permission);
        this.repository = repository;
    }

    @Override
    public AccessPermission execute(Message message) throws RuntimeException {
        if (repository == null)
            throw new RuntimeException("UserRepository must not be null!");
        AccessPermission permission = (AccessPermission) getMessage();
        //...
        Optional<User> exist = repository.findByUsername(permission.getUsername());
        if (exist.isPresent()){
            List<Role> roles = new ArrayList<>(exist.get().getRoles());
            List<Policy> policiesFrmRole = roles.stream()
                    .flatMap(role -> role.getPolicies().stream())
                    .collect(Collectors.toList());
            //
            List<Policy> policies = new ArrayList<>(policiesFrmRole);
            if (exist.get().getPolicies() != null) policies.addAll(exist.get().getPolicies());
            //
            List<Statement> statements = policies.stream()
                    .flatMap(policy -> policy.getStatements().stream())
                    .collect(Collectors.toList());
            //
            String resource = permission.getStatement().getResource();
            List<Action> matchedAction = statements.stream()
                    .filter(statement -> {
                        if (statement.getResource().endsWith("/**")){
                            String strippedPath = removePatternFromEnd(statement.getResource(), "/**");
                            return resource.startsWith(strippedPath);
                        }else {
                            return resource.equalsIgnoreCase(statement.getResource());
                        }
                    })
                    .map(statement -> statement.getAction())
                    .collect(Collectors.toList());
            //
            Action saved = Action.maxInOrder(matchedAction);
            Action asking = permission.getStatement().getAction();
            //Action.Write = 0; Action.Read=1; Action.None=2;
            //AskingPermission either equal or greater then SavedPermission:
            boolean isGranted = (asking != Action.None) && (asking.ordinal() >= saved.ordinal());
            return (!isGranted)
                    ? (AccessPermission) permission.setMessage("Action Didn't Matched").setStatus(401)
                    : (AccessPermission) permission.setMessage("Action Matched").setStatus(200);
        }
        //...
        return (AccessPermission) permission.setMessage("Action Didn't Matched").setStatus(401);
    }

    protected String removePatternFromEnd(String target, String pattern){
        if (target.endsWith(pattern)){
            return target.substring(0, target.length() - pattern.length());
        }
        return target;
    }

    @Override
    public AccessPermission abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return (AccessPermission) new AccessPermission().setMessage(reason).setStatus(500);
    }
}
