package com.skillstorm.taxprepsystemapi.dtos.in;


import com.skillstorm.taxprepsystemapi.models.Location;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
