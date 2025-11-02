package com.maheshbhatt.financialnuggets.repository;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<HoldingEntity, Long> {

    List<HoldingEntity> findByAmcId(Long amcId);

    List<HoldingEntity> findBySchemeCode(String schemeCode);

    List<HoldingEntity> findByAmcIdAndSchemeCode(Long amcId, String schemeCode);

    List<HoldingEntity> findByReportingDate(LocalDate reportingDate);

    List<HoldingEntity> findByAmcIdAndSchemeCodeAndReportingDate(Long amcId, String schemeCode, LocalDate reportingDate);

    Optional<HoldingEntity> findByIsinAndReportingDate(String isin, LocalDate reportingDate);

    // Phase 1: Progression queries
    List<HoldingEntity> findByIsinOrderByReportingDateAsc(String isin);

    List<HoldingEntity> findByIsinAndReportingDateBetweenOrderByReportingDateAsc(String isin, LocalDate startDate, LocalDate endDate);

    List<HoldingEntity> findBySchemeCodeAndReportingDateBetweenOrderByReportingDateAsc(String schemeCode, LocalDate startDate, LocalDate endDate);

    List<HoldingEntity> findByAmcIdAndReportingDateBetweenOrderByReportingDateAsc(Long amcId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT h.reportingDate FROM HoldingEntity h ORDER BY h.reportingDate DESC")
    List<LocalDate> findDistinctReportingDatesOrderByReportingDateDesc();
}
