package com.microservice.erp.domain.dto;


import lombok.Getter;
import lombok.Setter;

/**
 * Created By zepaG on 3/13/2022.
 */
@Setter
@Getter
public class CitizenDetailDto {
    //region private variables
    private String fullName;
    private String genderName;
    private Character gender;
     private String cid;
    private String dob;
    private String fatherName;
    private String fatherCid;
    private String motherName;
    private String motherCid;
    private String villageName;
    private String geogName;
    private String dzongkhagName;
    private String houseNo;
    private String thramNo;
    private String guardianNameFirst;
    private String guardianCidFirst;
    private String guardianNameSecond;
    private String guardianCidSecond;
    //endregion

}
