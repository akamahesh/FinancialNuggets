package com.maheshbhatt.financialnuggets.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SchemeDTO {
    private Long id;
    private String amcId;
    private String schemeCode;
    private String name;
    private String type;
    private String category;
    private String schemeNavName;
}
