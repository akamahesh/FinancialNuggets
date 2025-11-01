package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.SchemeEntity;
import com.maheshbhatt.financialnuggets.exception.AmcIdMissingException;
import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.repository.AmcRepostiory;
import com.maheshbhatt.financialnuggets.repository.SchemeRepository;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemeServiceImpl implements SchemeService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private AmcRepostiory amcRepostiory;

    @Override
    public SchemeDTO save(SchemeDTO schemeRequestDTO) {
        SchemeEntity entity = new SchemeEntity();
        if (schemeRequestDTO.getAmcId() == null || schemeRequestDTO.getAmcId().isEmpty()) {
            throw new AmcIdMissingException("Amc Id is missing in the request");
        }
//        AmcEntity amcEntity = amcRepostiory.findById()

        BeanUtils.copyProperties(schemeRequestDTO, entity);
        SchemeEntity saved = schemeRepository.save(entity);
        SchemeDTO responseDTO = new SchemeDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        return responseDTO;
    }

    @Override
    public List<SchemeDTO> getAllSchemes() {
        List<SchemeEntity> schemeEntities = schemeRepository.findAll();
        List<SchemeDTO> schemeDTOs = schemeEntities.stream().map(entity -> {
            SchemeDTO dto = new SchemeDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
        return schemeDTOs;
    }

    @Override
    public List<SchemeDTO> getAllSchemesByAmcId(Long id) {
        List<SchemeEntity> schemeEntities = schemeRepository.findAll();
        List<SchemeDTO> schemeDTOs = schemeEntities.stream().filter(sch -> sch.getAmcId().equals(id)).map(entity -> {
            SchemeDTO dto = new SchemeDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toList();
        return schemeDTOs;
    }

    @Override
    public SchemeDTO getSchemeById(Long id) {
        return null;
    }

    @Override
    public SchemeDTO deleteSchemeById(Long id) {
        return null;
    }
}
