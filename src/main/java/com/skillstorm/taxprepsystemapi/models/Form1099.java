package com.skillstorm.taxprepsystemapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Document

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form1099 {

    @Id
    @GeneratedValue
    private BigInteger id;

    // XXX-XX-XXXX
    private String payerTIN;

    private Location location;

    private Double income;
}
