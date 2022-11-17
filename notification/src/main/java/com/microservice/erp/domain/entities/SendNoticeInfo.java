package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "notice_send_info")
@AttributeOverride(name = "id", column = @Column(name = "notice_send_info_id"))
public class SendNoticeInfo extends Auditable<Long, Long> {

    //region private variables
    @NotNull()
    @Column(name = "notice_configuration_id")
    private Long noticeConfigurationId;
    @NotNull(message = "Year cannot be null.")
    @Basic(optional = false)
    @Column(name = "year", columnDefinition = "varchar(4)")
    private String year;

    @NotNull(message = "Notice configuration name cannot be null.")
    @Basic(optional = false)
    @Column(name = "notice_name", columnDefinition = "varchar(255)")
    private String noticeName;

    @Column(name = "notice_body", columnDefinition = "varchar(10000)")
    private String noticeBody;

    @Basic(optional = false)
    @Column(name = "class_id", columnDefinition = "char(2)")
    private String classId;

    @Basic(optional = false)
    @Column(name = "age")
    private Integer age;

    @Basic(optional = false)
    @Column(name = "send_sms")
    private Boolean sendSms;

    @Basic(optional = false)
    @Column(name = "send_email")
    private Boolean sendEmail;
    //endregion

    //region setters and getters

    public Long getNoticeConfigurationId() {
        return noticeConfigurationId;
    }

    public void setNoticeConfigurationId(Long noticeConfigurationId) {
        this.noticeConfigurationId = noticeConfigurationId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeBody() {
        return noticeBody;
    }

    public void setNoticeBody(String noticeBody) {
        this.noticeBody = noticeBody;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSendSms() {
        return sendSms;
    }

    public void setSendSms(Boolean sendSms) {
        this.sendSms = sendSms;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    //endregion
}
