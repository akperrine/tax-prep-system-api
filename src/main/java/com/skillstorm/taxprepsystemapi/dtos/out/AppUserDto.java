package com.skillstorm.taxprepsystemapi.dtos.out;

import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.AppUserInformation;
import com.skillstorm.taxprepsystemapi.models.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Date dob;

    private Location location;

    private AppUserInformation appUserInformation;


    public AppUserDto(AppUser appUser) {
        setId(appUser.getId());
        setFirstName(appUser.getFirstName());
        setLastName(appUser.getLastName());
        setEmail(appUser.getEmail());
        setDob(appUser.getDob());
        setLocation(appUser.getLocation());
        setAppUserInformation(appUser.getAppUserInformation());
    }


}
