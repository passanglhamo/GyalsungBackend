package com.microservice.erp.domain.helper;

import java.util.List;

public enum Action {
    Write("write"),
    Read("read"),
    None("none");

    private String val;
    Action(String val) {
        this.val = val;
    }

    public String value(){
        return this.val;
    }

    public static Action maxInOrder(List<Action> actions){
        //actions.sort((a, b) -> a.ordinal() < b.ordinal() ? -1 : 1); //Sort Ascending order.
        actions.sort((a, b) -> a.compareTo(b)); //Sort Ascending order.
        return (actions.size() > 0) ? actions.get(0) : None;
    }
}
