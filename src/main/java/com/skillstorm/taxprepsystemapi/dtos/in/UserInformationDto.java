package com.skillstorm.taxprepsystemapi.dtos.in;

import lombok.Data;

import java.util.Date;

@Data
public class UserInformationDto {
    private String ssn;
    private Date dob;
}
