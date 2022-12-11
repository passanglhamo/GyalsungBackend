package com.microservice.erp.domain.tasks.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.NewAccountRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Optional;

public class CheckUserExist extends AbstractTask<Message, Response> {

    private NewAccountRequest accountRequest;
    private UserRepository repository;

    public CheckUserExist(UserRepository repository, String username) {
        super(new Property("username", username));
        this.repository = repository;
    }

    public CheckUserExist(UserRepository repository, NewAccountRequest account) {
        this(repository, account.getUsername());
        this.accountRequest = account;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        String username = (accountRequest != null) ? accountRequest.getUsername() : getPropertyValue("username").toString();
        if (repository != null){
            Optional<User> optUser = repository.findByUsername(username);
            if (optUser.isPresent()){
                return (accountRequest == null)
                        ? new NewAccountRequest(username, null, null, null)
                                                .setStatus(200).setMessage(username + " exist.")
                        : accountRequest.setStatus(200).setMessage(username + " exist.");
            }else {
                return accountRequest.setStatus(404).setMessage(username + " doesn't exist.");
            }
        }
        throw new RuntimeException("UserRepository Must Not Be Null");
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
