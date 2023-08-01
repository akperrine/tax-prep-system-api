package com.skillstorm.taxprepsystemapi.dtos.in;

import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
public class AppUserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private Date dob;
    private Location location;
    private String password;

    public static AppUserDto makeAppUserDto(AppUser appUser) {
        return AppUserDto.builder()
            .id(String.valueOf(appUser.getId()))
            .firstName(appUser.getFirstName())
            .lastName(appUser.getLastName())
            .email(appUser.getEmail())
            .ssn(appUser.getSsn())
            .dob(appUser.getDob())
            .location(appUser.getLocation())
            .password(appUser.getPassword())
            .build();
    }
}
