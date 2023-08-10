package com.skillstorm.taxprepsystemapi.models;

import com.skillstorm.taxprepsystemapi.enums.FilingStatus;
import com.skillstorm.taxprepsystemapi.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private FilingStatus filingStatus;

    private MaritalStatus maritalStatus;

    private Date filed;

    private List<FormW2> formW2s = new ArrayList<>();

    private List<Form1099> form1099s = new ArrayList<>();
}
