package com.skillstorm.taxprepsystemapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormW2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    //XX-XXXXXXX
    private String employerEIN;

    private Double income;

    private Double withheld;

    private Location location;
}
