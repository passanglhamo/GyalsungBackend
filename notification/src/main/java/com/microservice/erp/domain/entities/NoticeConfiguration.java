package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_notice_configuration")
@AttributeOverride(name = "id", column = @Column(name = "notice_configuration_id"))
public class NoticeConfiguration extends Auditable<Long, Long> {

    @NotNull(message = "Notice configuration name cannot be null.")
    @Basic(optional = false)
    @Column(name = "notice_name", columnDefinition = "varchar(255)")
    private String noticeName;

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

}
