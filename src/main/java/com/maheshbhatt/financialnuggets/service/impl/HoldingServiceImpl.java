package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.HoldingEntity;
import com.maheshbhatt.financialnuggets.exception.HoldingNonFoundException;
import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.repository.HoldingRepository;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        HoldingEntity saved = holdingRepository.save(entity);
        HoldingDTO savedDTO = new HoldingDTO();
        BeanUtils.copyProperties(saved, savedDTO);
        return savedDTO;
    }

    @Override
    public List<HoldingDTO> getAllHoldings() {
        List<HoldingEntity> entities = holdingRepository.findAll();
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }

    @Override
    public HoldingDTO getHoldingById(Long id) {
        Optional<HoldingEntity> optional = holdingRepository.findById(id);
        if (optional.isEmpty()) {
            throw new HoldingNonFoundException("Holding with id " + id + " not found");
        }
        HoldingDTO dto = new HoldingDTO();
        BeanUtils.copyProperties(optional.get(), dto);
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
        HoldingEntity saved = holdingRepository.save(entity);
        HoldingDTO responseDTO = new HoldingDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        return responseDTO;
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcId(Long amcId) {
        List<HoldingEntity> entities = holdingRepository.findByAmcId(amcId);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsBySchemeCode(String schemeCode) {
        List<HoldingEntity> entities = holdingRepository.findBySchemeCode(schemeCode);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcIdAndSchemeCode(Long amcId, String schemeCode) {
        List<HoldingEntity> entities = holdingRepository.findByAmcIdAndSchemeCode(amcId, schemeCode);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByReportingDate(LocalDate reportingDate) {
        List<HoldingEntity> entities = holdingRepository.findByReportingDate(reportingDate);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }

    @Override
    public List<HoldingDTO> getHoldingsByAmcIdAndSchemeCodeAndReportingDate(Long amcId, String schemeCode, LocalDate reportingDate) {
        List<HoldingEntity> entities = holdingRepository.findByAmcIdAndSchemeCodeAndReportingDate(amcId, schemeCode, reportingDate);
        return entities.stream().map(entity -> {
            HoldingDTO dto = new HoldingDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
    }
}
