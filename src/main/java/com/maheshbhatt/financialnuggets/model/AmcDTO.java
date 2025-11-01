package com.maheshbhatt.financialnuggets.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmcDTO {
    private Long id;
    private String amcId;
    private String amcName;
    private String shortName;
    private String websiteUrl;
    private String shortDescription;
    private String description;
    private String imgUrl;
}
