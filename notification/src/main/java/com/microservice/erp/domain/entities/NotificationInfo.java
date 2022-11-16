/*
package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

*/
/**
 * @author Rajib Kumer Ghosh
 *
 *//*


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "NOT_NOTIFICATION_INFO")
@AttributeOverride(name = "id", column = @Column(name = "NOTIFICATION_ID"))
public class NotificationInfo extends Auditable<Long, Long> {

    @Column(name = "NOTIFICATION_NAME")
    private String notificationName;

    @Column(name = "ELIGIBLE_TO_CLASS")
    private String eligibleToClass;

    @Column(name = "ELIGIBLE_TO_AGE")
    private String eligibleToAge;

    @NotNull(message = "Mail cannot be null")
    @Basic(optional = false)
    @Column(name = "SEND_MAIL")
    private String sendMail;

    @NotNull(message = "SMS cannot be null")
    @Basic(optional = false)
    @Column(name = "SEND_SMS")
    private String sendSms;

    @NotNull
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
}
*/
