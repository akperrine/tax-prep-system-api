package com.skillstorm.taxprepsystemapi.models;

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
public class FormW2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double income;

    @OneToOne
    private Location location;

    @OneToMany
    @JoinTable(
            name = "formw2_deduction",
            joinColumns = { @JoinColumn(name = "formw2_id")},
            inverseJoinColumns = {@JoinColumn(name = "deduction_id")}
    )
    private List<Deduction> deductions = new ArrayList<>();

}
