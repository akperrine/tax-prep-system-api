package com.skillstorm.taxprepsystemapi.dtos.in;

import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AppUserInDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private Date dob;
    private String username;
    private Location location;
    private String password;

    public static AppUserInDto makeAppUserDto(AppUser appUser) {
        return AppUserInDto.builder()
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