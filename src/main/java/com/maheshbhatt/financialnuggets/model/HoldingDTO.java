package com.maheshbhatt.financialnuggets.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HoldingDTO {
    private Long id;
    private Long amcId;
    private String schemeCode;
    private String name;
    private String isin;
    private Long quantity;
    private BigDecimal marketValue;
    private Long percentageOfAum;
    private String reportingDate;
}
