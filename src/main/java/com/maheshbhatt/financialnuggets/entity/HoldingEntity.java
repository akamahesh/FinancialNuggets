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
@Table(name = "holdings", uniqueConstraints = @UniqueConstraint(columnNames = {"isin", "reportingDate"}))
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HoldingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long amcId;
    @Column(nullable = false)
    private String schemeCode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal marketValue;

    @Column(nullable = false)
    private BigDecimal percentageOfAum;

    @Column(nullable = false)
    private LocalDate reportingDate;

}
