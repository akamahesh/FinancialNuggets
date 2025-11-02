package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import com.maheshbhatt.financialnuggets.exception.HoldingNonFoundException;
import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.repository.HoldingRepository;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.utils.CsvReader;
import com.maheshbhatt.financialnuggets.utils.DateParser;
import com.maheshbhatt.financialnuggets.utils.MoneyUtils;
import com.maheshbhatt.financialnuggets.utils.PercentageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HoldingServiceImpl implements HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    @Override
    public HoldingDTO save(HoldingDTO holdingDTO) {
        // Check if a record with the same isin and reportingDate already exists
        Optional<HoldingEntity> existing = holdingRepository
                .findByIsinAndReportingDate(holdingDTO.getIsin(), holdingDTO.getReportingDate());

        HoldingEntity entity;
        if (existing.isPresent()) {
            // Update existing record
            entity = existing.get();
            BeanUtils.copyProperties(holdingDTO, entity, "id", "isin", "reportingDate");
        } else {
            // Create new record
            entity = new HoldingEntity();
            BeanUtils.copyProperties(holdingDTO, entity);
        }

        // Handle percentageOfAum conversion from Long to BigDecimal
        if (holdingDTO.getPercentageOfAum() != null) {
            entity.setPercentageOfAum(holdingDTO.getPercentageOfAum());
        }

        // Handle percentageOfAum conversion from Long to BigDecimal
        if (holdingDTO.getPercentageOfAum() != null) {
            entity.setPercentageOfAum(holdingDTO.getPercentageOfAum());
        }
        HoldingEntity saved = holdingRepository.save(entity);
        HoldingDTO savedDTO = new HoldingDTO();
        BeanUtils.copyProperties(saved, savedDTO);
        return savedDTO;
    }

    @Override
    public List<HoldingDTO> saveAll(List<HoldingDTO> holdings) {
        List<HoldingDTO> savedHoldings = new ArrayList<>();
        for (HoldingDTO holdingDTO : holdings) {
            savedHoldings.add(save(holdingDTO));
        }
        return savedHoldings;
    }

    @Override
    public List<HoldingDTO> getAllHoldings() {
        List<HoldingEntity> entities = holdingRepository.findAll();
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }
            return dto;
        }).toList();
    }

    @Override
    public HoldingDTO getHoldingById(Long id) {
        Optional<HoldingEntity> optional = holdingRepository.findById(id);
        if (optional.isEmpty()) {
            throw new HoldingNonFoundException("Holding with id " + id + " not found");
        }
        HoldingEntity entity = optional.get();
        HoldingDTO dto = new HoldingDTO();
        BeanUtils.copyProperties(entity, dto);
        // Convert BigDecimal to Long for percentageOfAum
        if (entity.getPercentageOfAum() != null) {
            dto.setPercentageOfAum(entity.getPercentageOfAum());
        }

        return dto;
    }

    @Override
    public void deleteHoldingById(Long id) {
        if (!holdingRepository.existsById(id)) {
            throw new RuntimeException("Holding with id " + id + " not found");
        }
        holdingRepository.deleteById(id);
    }

    @Override
    public String deleteAll() {
        holdingRepository.deleteAll();
        return "All Deleted";
    }

    @Override
    public HoldingDTO updateHolding(Long id, HoldingDTO holdingDTO) {
        Optional<HoldingEntity> optional = holdingRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Holding with id " + id + " not found");
        }
        HoldingEntity entity = optional.get();
        BeanUtils.copyProperties(holdingDTO, entity, "id");
        // Handle percentageOfAum conversion from Long to BigDecimal
        if (holdingDTO.getPercentageOfAum() != null) {
            entity.setPercentageOfAum(holdingDTO.getPercentageOfAum());
        }

        HoldingEntity saved = holdingRepository.save(entity);
        HoldingDTO responseDTO = new HoldingDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        // Convert BigDecimal back to Long for percentageOfAum
        if (saved.getPercentageOfAum() != null) {
            responseDTO.setPercentageOfAum(saved.getPercentageOfAum());
        }
        return responseDTO;
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcId(Long amcId) {
        List<HoldingEntity> entities = holdingRepository.findByAmcId(amcId);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }

            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsBySchemeCode(String schemeCode) {
        List<HoldingEntity> entities = holdingRepository.findBySchemeCode(schemeCode);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcIdAndSchemeCode(Long amcId, String schemeCode) {
        List<HoldingEntity> entities = holdingRepository.findByAmcIdAndSchemeCode(amcId, schemeCode);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }

            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByReportingDate(LocalDate reportingDate) {
        List<HoldingEntity> entities = holdingRepository.findByReportingDate(reportingDate);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcIdAndSchemeCodeAndReportingDate(Long amcId, String schemeCode, LocalDate reportingDate) {
        List<HoldingEntity> entities = holdingRepository.findByAmcIdAndSchemeCodeAndReportingDate(amcId, schemeCode, reportingDate);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            // Convert BigDecimal to Long for percentageOfAum
            if (entity.getPercentageOfAum() != null) {
                dto.setPercentageOfAum(entity.getPercentageOfAum());
            }
            return dto;
        }).toList();
    }


    @Override
    public List<HoldingDTO> parseHoldingCsv(MultipartFile file, Long amcId, String schemeCode) {
        try {
            List<String[]> csvRows = CsvReader.readCsv(file.getInputStream());
            String reportingDateStr = csvRows.get(3)[1];
            LocalDate reportingDate = DateParser.parseLocalDateOrThrow(reportingDateStr);

            List<HoldingDTO> holdings = new ArrayList<>();
            for (int i = 7; i < csvRows.size() - 1; i++) {
                String[] row = csvRows.get(i);
                String name = row[1] == null ? "" : row[1].trim();
                String isin = row[2] == null ? "" : row[2].trim();
                if (name.isEmpty() || isin.isEmpty()) {
                    break;
                }
                String industry = row[3] == null ? "" : row[3].trim();
                Long quantityStr = Long.valueOf(row[4].replace(",", ""));
                BigDecimal mvInRupees = MoneyUtils.fromLakhsToRupees(MoneyUtils.parseIndianAmount(row[5]));
                BigDecimal percentageOfAum = PercentageUtils.parsePercentToFraction(row[6]);

                HoldingDTO holdingDTO = new HoldingDTO();
                holdingDTO.setAmcId(amcId);
                holdingDTO.setSchemeCode(schemeCode);
                holdingDTO.setName(name);
                holdingDTO.setIsin(isin);
                holdingDTO.setMarketValue(mvInRupees);
                holdingDTO.setPercentageOfAum(percentageOfAum);
                holdingDTO.setQuantity(quantityStr);
                holdingDTO.setIndustry(industry);
                holdingDTO.setReportingDate(reportingDate);
                holdings.add(holdingDTO);
            }
            List<HoldingDTO> holdingEnitites = holdings.stream().map(this::save).toList();
            return holdingEnitites;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
