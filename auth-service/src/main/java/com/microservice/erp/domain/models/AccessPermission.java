package com.microservice.erp.domain.models;

import com.microservice.erp.domain.entities.Statement;
import com.infoworks.lab.rest.models.Response;

public class AccessPermission extends Response {
    private String username;
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
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
