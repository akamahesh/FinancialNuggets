package com.maheshbhatt.financialnuggets.repository;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HoldingRepository extends JpaRepository<HoldingEntity, Long> {

    List<HoldingEntity> findByAmcId(Long amcId);

    List<HoldingEntity> findBySchemeCode(String schemeCode);

    List<HoldingEntity> findByAmcIdAndSchemeCode(Long amcId, String schemeCode);

    List<HoldingEntity> findByReportingDate(LocalDate reportingDate);

    List<HoldingEntity> findByAmcIdAndSchemeCodeAndReportingDate(Long amcId, String schemeCode, LocalDate reportingDate);
}
