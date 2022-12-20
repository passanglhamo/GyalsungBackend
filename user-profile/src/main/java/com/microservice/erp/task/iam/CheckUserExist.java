package com.microservice.erp.task.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.model.NewAccountRequest;


import java.util.Optional;

public class CheckUserExist extends AbstractTask<Message, Response> {

    private NewAccountRequest accountRequest;
    private ISaUserRepository repository;

    public CheckUserExist(ISaUserRepository repository, String username) {
        super(new Property("username", username));
        this.repository = repository;
    }

    public CheckUserExist(ISaUserRepository repository, NewAccountRequest account) {
        this(repository, account.getUsername());
        this.accountRequest = account;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        String username = (accountRequest != null) ? accountRequest.getUsername() : getPropertyValue("username").toString();
        if (repository != null) {
            Optional<SaUser> optUser;
            optUser = Optional.ofNullable(repository.findByUsername(username));//login using username
            if (!optUser.isPresent()) {
                optUser = Optional.ofNullable(repository.findByCid(username));//login using cid
            }
            if (!optUser.isPresent()) {
                optUser = Optional.ofNullable(repository.findByEmail(username));//login using email
            }
            if (optUser.isPresent()) {
                return (accountRequest == null)
                        ? new NewAccountRequest(username, null, null, null)
                        .setStatus(200).setMessage(username + " exist.")
                        : accountRequest.setStatus(200).setMessage(username + " exist.");
            } else {
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
