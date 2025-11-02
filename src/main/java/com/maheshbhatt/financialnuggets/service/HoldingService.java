package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface HoldingService {
    HoldingDTO save(HoldingDTO holdingDTO);
    List<HoldingDTO> saveAll(List<HoldingDTO> holdings);
    List<HoldingDTO> getAllHoldings();
    HoldingDTO getHoldingById(Long id);
    void deleteHoldingById(Long id);
    HoldingDTO updateHolding(Long id, HoldingDTO holdingDTO);
    List<HoldingDTO> getHoldingsByAmcId(Long amcId);
    List<HoldingDTO> getHoldingsBySchemeCode(String schemeCode);
    List<HoldingDTO> getHoldingsByAmcIdAndSchemeCode(Long amcId, String schemeCode);
    List<HoldingDTO> getHoldingsByReportingDate(LocalDate reportingDate);
    List<HoldingDTO> getHoldingsByAmcIdAndSchemeCodeAndReportingDate(Long amcId, String schemeCode, LocalDate reportingDate);

    List<HoldingDTO> parseHoldingCsv(MultipartFile file, Long amcId, String schemeCode);
}
