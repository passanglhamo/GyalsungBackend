package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;

@Entity(name = "sa_screen")
@AttributeOverride(name = "id", column = @Column(name = "screen_id", columnDefinition = "bigint"))
public class Screen extends Auditable<BigInteger, Long> {

    //region private variables
    @Column(name = "screen_group_id", columnDefinition = "bigint")
    private BigInteger screenGroupId;

    @Column(name = "screen_name", columnDefinition = "varchar(255)")
    private String screenName;

    @Column(name = "screen_url", columnDefinition = "varchar(255)")
    private String screenUrl;

    @Column(name = "screen_icon_name", columnDefinition = "varchar(255)")
    private String screenIconName;
    //endregion

    //region setters and getters

    public BigInteger getScreenGroupId() {
        return screenGroupId;
    }

    public void setScreenGroupId(BigInteger screenGroupId) {
        this.screenGroupId = screenGroupId;
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

    public String getScreenIconName() {
        return screenIconName;
    }

    public void setScreenIconName(String screenIconName) {
        this.screenIconName = screenIconName;
    }

    //endregion
}
