package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class RegistrationDateDto {
    //region private variables
    private Date trainingDate;
    private Character status;
    //endregion
}
