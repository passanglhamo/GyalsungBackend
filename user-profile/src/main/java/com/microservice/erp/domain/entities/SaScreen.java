package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

@Entity(name = "sa_screen")
public class SaScreen {

    //region private variables
    @Id
    @Column(name = "screen_id", columnDefinition = "varchar(255)")
    private String screenId;

    @Column(name = "screen_name", columnDefinition = "varchar(255)")
    private String screenName;

    @Column(name = "screen_url", columnDefinition = "varchar(255)")
    private String screenUrl;
    //endregion

    //region setters and getters

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }

    //endregion
}
