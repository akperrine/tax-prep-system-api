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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(
            name = "app_user_information_tax_document",
            joinColumns = { @JoinColumn(name = "app_user_information_id")},
            inverseJoinColumns = {@JoinColumn(name = "tax_document_id")}
    )
    private List<TaxDocument> taxDocuments = new ArrayList<>();

}
