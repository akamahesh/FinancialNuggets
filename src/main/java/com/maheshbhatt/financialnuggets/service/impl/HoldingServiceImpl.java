package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import com.maheshbhatt.financialnuggets.exception.HoldingNonFoundException;
import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.repository.HoldingRepository;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.utils.CsvReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HoldingServiceImpl implements HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    @Override
    public HoldingDTO save(HoldingDTO holdingDTO) {
        HoldingEntity entity = new HoldingEntity();
        BeanUtils.copyProperties(holdingDTO, entity);
        // Handle percentageOfAum conversion from Long to BigDecimal
        if (holdingDTO.getPercentageOfAum() != null) {
            entity.setPercentageOfAum(holdingDTO.getPercentageOfAum());
        }
        // Handle reportingDate conversion from String to LocalDate
        if (holdingDTO.getReportingDate() != null && !holdingDTO.getReportingDate().isEmpty()) {
            try {
                entity.setReportingDate(LocalDate.parse(holdingDTO.getReportingDate()));
            } catch (DateTimeParseException e) {
                // If parsing fails, leave it null
            }
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
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
        // Convert LocalDate to String for reportingDate
        if (entity.getReportingDate() != null) {
            dto.setReportingDate(entity.getReportingDate().toString());
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
        // Handle reportingDate conversion from String to LocalDate
        if (holdingDTO.getReportingDate() != null && !holdingDTO.getReportingDate().isEmpty()) {
            try {
                entity.setReportingDate(LocalDate.parse(holdingDTO.getReportingDate()));
            } catch (DateTimeParseException e) {
                // If parsing fails, leave it null
            }
        }
        HoldingEntity saved = holdingRepository.save(entity);
        HoldingDTO responseDTO = new HoldingDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        // Convert BigDecimal back to Long for percentageOfAum
        if (saved.getPercentageOfAum() != null) {
            responseDTO.setPercentageOfAum(saved.getPercentageOfAum().longValue());
        }
        // Convert LocalDate back to String for reportingDate
        if (saved.getReportingDate() != null) {
            responseDTO.setReportingDate(saved.getReportingDate().toString());
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
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
                dto.setPercentageOfAum(entity.getPercentageOfAum().longValue());
            }
            // Convert LocalDate to String for reportingDate
            if (entity.getReportingDate() != null) {
                dto.setReportingDate(entity.getReportingDate().toString());
            }
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> previewHoldingCsv(MultipartFile file, Long amcId, String schemeCode) {
        try {
            List<String[]> csvRows = CsvReader.readCsv(file.getInputStream());

            // Expected CSV format (skip header):
            // name, isin, quantity, marketValue, percentageOfAum, reportingDate
            // or: name, isin, quantity, marketValue, percentageOfAum

            List<HoldingDTO> holdings = new ArrayList<>();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 1; i < csvRows.size(); i++) { // Skip header row
                String[] row = csvRows.get(i);
                if (row.length < 3) continue; // Skip invalid rows

                HoldingDTO holding = new HoldingDTO();
                holding.setAmcId(amcId);
                holding.setSchemeCode(schemeCode);

                // Parse CSV columns
                int colIndex = 0;
                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    holding.setName(row[colIndex].trim());
                }
                colIndex++;

                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    holding.setIsin(row[colIndex].trim());
                }
                colIndex++;

                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    try {
                        holding.setQuantity(Long.parseLong(row[colIndex].trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid quantity
                        continue;
                    }
                }
                colIndex++;

                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    try {
                        holding.setMarketValue(new BigDecimal(row[colIndex].trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid market value
                        continue;
                    }
                }
                colIndex++;

                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    try {
                        BigDecimal percentage = new BigDecimal(row[colIndex].trim());
                        holding.setPercentageOfAum(percentage.longValue());
                    } catch (NumberFormatException e) {
                        // Skip invalid percentage
                        continue;
                    }
                }
                colIndex++;

                if (row.length > colIndex && row[colIndex] != null && !row[colIndex].trim().isEmpty()) {
                    try {
                        LocalDate.parse(row[colIndex].trim(), dateFormatter);
                        holding.setReportingDate(row[colIndex].trim());
                    } catch (DateTimeParseException e) {
                        // If date parsing fails, leave it null
                    }
                }

                holdings.add(holding);
            }

            return holdings;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }
    }
}
