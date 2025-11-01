package com.maheshbhatt.financialnuggets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "amcs")
public class AmcEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String amcName;

    private String amcId;

    private String shortName;
    private String shortDescription;
    private String description;
    private String imgUrl;
    private String websiteUrl;
    @CreationTimestamp
    private Date createAt;
    @UpdateTimestamp
    private Date updateAt;

    @OneToMany(mappedBy = "amc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemeEntity> schemes;

}
