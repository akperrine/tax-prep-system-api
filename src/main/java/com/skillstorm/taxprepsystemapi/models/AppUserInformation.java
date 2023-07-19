package com.skillstorm.taxprepsystemapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skillstorm.taxprepsystemapi.enums.FilingStatus;
import com.skillstorm.taxprepsystemapi.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInformation {

    @Id
    private String ssn;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private AppUser appUser;

    @OneToMany
    @JoinTable(
            name = "app_user_information_form1099",
            joinColumns = { @JoinColumn(name = "app_user_information_id")},
            inverseJoinColumns = {@JoinColumn(name = "form1099_id")}
    )
    private List<TaxDocument> taxDocuments = new ArrayList<>();

}
