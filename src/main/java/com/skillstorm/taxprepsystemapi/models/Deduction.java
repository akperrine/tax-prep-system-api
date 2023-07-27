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
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String name;

    private Double amount = 0d;
}
