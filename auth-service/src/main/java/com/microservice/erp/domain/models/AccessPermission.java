package com.microservice.erp.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.erp.domain.entities.Statement;
import com.infoworks.lab.rest.models.Response;

public class AccessPermission extends Response {
    private String username;
    private String resource;
    private String action = Action.None.value();

    @JsonIgnore
    private Statement statement;

    public AccessPermission() {}

    public AccessPermission(String username, String resource, Action action) {
        this.username = username;
        this.statement = new Statement();
        this.statement.setAction(action);
        this.statement.setResource(resource);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Statement getStatement() {
        if (resource != null && !resource.isEmpty() && action != null && !action.isEmpty()) {
            this.statement = new Statement();
            this.statement.setAction(Action.valueOf(action));
            this.statement.setResource(resource);
        }
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @JsonIgnore
    private Integer status = 200;

    @JsonIgnore
    private String error;

    @JsonIgnore
    private String message;

    @JsonIgnore
    private String payload;
}
