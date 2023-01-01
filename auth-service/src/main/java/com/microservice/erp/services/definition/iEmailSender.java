package com.microservice.erp.services.definition;

import com.microservice.erp.domain.beans.models.Email;

public interface iEmailSender {
    int sendHtmlMessage(Email email);
}
