package com.maheshbhatt.financialnuggets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "holdings")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HoldingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amcId;
    private String schemeCode;
    private String name;
    private String isin;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long marketValue;

    @Column(nullable = false)
    private Long percentageOfAum;

    private LocalDate reportingDate;

}
