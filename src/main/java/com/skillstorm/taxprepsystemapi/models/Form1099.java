package com.skillstorm.taxprepsystemapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form1099 {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Location location;

    @Column(nullable = false)
    private Double income;
}
