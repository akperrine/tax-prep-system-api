package com.skillstorm.taxprepsystemapi.dtos.out;

import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserOutDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Date dob;

    private String ssn;

    private Location location;

    //private AppUserInformation appUserInformation;
    private List<TaxDocument> taxDocuments;

    public AppUserOutDto(AppUser appUser) {
        setId(String.valueOf(appUser.getId()));
        setFirstName(appUser.getFirstName());
        setLastName(appUser.getLastName());
        setEmail(appUser.getEmail());
        setDob(appUser.getDob());
        setLocation(appUser.getLocation());
        setSsn(appUser.getSsn());
        setTaxDocuments(appUser.getTaxDocuments());
        //setAppUserInformation(appUser.getAppUserInformation());
    }
}
