package com.maheshbhatt.financialnuggets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "schemes")
public class SchemeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amc_id", referencedColumnName = "id", nullable = false)
    private AmcEntity amc;

    @Column(name = "code", nullable = false, unique = true)
    private String schemeCode;

    @Column(nullable = false)
    private String name;

    private String type;
    private String category;
    private String schemeNavName;
    private LocalDate launchDate;
    private LocalDate closureDate;

}
