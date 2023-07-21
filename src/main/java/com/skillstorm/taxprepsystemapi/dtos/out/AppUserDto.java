package com.skillstorm.taxprepsystemapi.dtos.out;

import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.AppUserInformation;
import com.skillstorm.taxprepsystemapi.models.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

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

    private String ssn;

    private Location location;

    private AppUserInformation appUserInformation;


    public AppUserDto(AppUser appUser) {
        setId(appUser.getId());
        setFirstName(appUser.getFirstName());
        setLastName(appUser.getLastName());
        setEmail(appUser.getEmail());
        setDob(appUser.getDob());
        setLocation(appUser.getLocation());
        setSsn(appUser.getSsn());
        setAppUserInformation(appUser.getAppUserInformation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserDto that = (AppUserDto) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(dob, that.dob) && Objects.equals(ssn, that.ssn) && Objects.equals(location, that.location) && Objects.equals(appUserInformation, that.appUserInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, dob, ssn, location, appUserInformation);
    }
}
