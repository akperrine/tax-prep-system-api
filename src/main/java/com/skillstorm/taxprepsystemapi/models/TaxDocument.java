package com.skillstorm.taxprepsystemapi.models;

import com.skillstorm.taxprepsystemapi.enums.FilingStatus;
import com.skillstorm.taxprepsystemapi.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private FilingStatus filingStatus;

    @Column(nullable = false)
    private MaritalStatus maritalStatus;

    @Column(nullable = true)
    private Date filed;

    @OneToMany
    @JoinTable(
            name = "tax_document_formw2",
            joinColumns = { @JoinColumn(name = "tax_document_id")},
            inverseJoinColumns = {@JoinColumn(name = "formw2_id")}
    )
    private List<FormW2> formW2s = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "tax_document_form1099",
            joinColumns = { @JoinColumn(name = "tax_document_id")},
            inverseJoinColumns = {@JoinColumn(name = "form1099_id")}
    )
    private List<Form1099> form1099s = new ArrayList<>();
}
