package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "not_notice_configuration")
@AttributeOverride(name = "id", column = @Column(name = "notice_configuration_id", columnDefinition = "bigint"))
public class NoticeConfiguration extends Auditable<BigInteger, Long> {

    @NotNull(message = "Notice configuration name cannot be null.")
    @Basic(optional = false)
    @Column(name = "notice_name", columnDefinition = "varchar(255)")
    private String noticeName;

    //@NotNull(message = "Notice configuration body cannot be null.")
    //@Basic(optional = false)
    @Column(name = "notice_body",columnDefinition = "varchar(255)")
    private String noticeBody;

    @Basic(optional = false)
    @Column(name = "class_id", columnDefinition = "char(2)")
    private String classId;

    @Basic(optional = false)
    @Column(name = "age" ,columnDefinition = "int")
    private Integer age;

    @Basic(optional = false)
    @Column(name = "send_sms")
    private Boolean sendSms;

    @Basic(optional = false)
    @Column(name = "send_email")
    private Boolean sendEmail;

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
}
