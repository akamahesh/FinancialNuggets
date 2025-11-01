package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.AmcEntity;
import com.maheshbhatt.financialnuggets.entity.SchemeEntity;
import com.maheshbhatt.financialnuggets.exception.AmcIdMissingException;
import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.repository.AmcRepostiory;
import com.maheshbhatt.financialnuggets.repository.SchemeRepository;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import com.maheshbhatt.financialnuggets.utils.CsvReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SchemeServiceImpl implements SchemeService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private AmcRepostiory amcRepostiory;

    @Override
    public SchemeDTO save(SchemeDTO schemeRequestDTO) {
        SchemeEntity entity = new SchemeEntity();
        if (schemeRequestDTO.getAmcId() == null) {
            throw new AmcIdMissingException("Amc Id is missing in the request");
        }

        // Fetch the amcEntity by id
        Optional<AmcEntity> amcOptional = amcRepostiory.findById(schemeRequestDTO.getAmcId());
        if (amcOptional.isEmpty()) {
            throw new AmcIdMissingException("Amc Entity not found for the given Id: " + schemeRequestDTO.getAmcId());
        }
        //set the amc relationship
        entity.setAmc(amcOptional.get());
        entity.setSchemeCode(schemeRequestDTO.getSchemeCode());
        entity.setName(schemeRequestDTO.getName());
        entity.setType(schemeRequestDTO.getType());
        entity.setCategory(schemeRequestDTO.getCategory());
        entity.setSchemeNavName(schemeRequestDTO.getSchemeNavName());

        SchemeEntity saved = schemeRepository.save(entity);
        SchemeDTO responseDTO = new SchemeDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        responseDTO.setAmcId(saved.getAmc().getId());
        return responseDTO;
    }

    @Override
    public List<SchemeDTO> getAllSchemes() {
        List<SchemeEntity> schemeEntities = schemeRepository.findAll();
        List<SchemeDTO> schemeDTOs = schemeEntities.stream().map(entity -> {
            SchemeDTO dto = new SchemeDTO();
            BeanUtils.copyProperties(entity, dto);
            // set amcid from the relationship
            if (entity.getAmc() != null) {
                dto.setAmcId(entity.getAmc().getId());
            }
            return dto;
        }).toList();
        return schemeDTOs;
    }

    @Override
    public List<SchemeDTO> getAllSchemesByAmcId(Long id) {
        List<SchemeEntity> schemeEntities = schemeRepository.findAll();
        List<SchemeDTO> schemeDTOs = schemeEntities.stream()
                .filter(sch -> sch.getAmc() != null && sch.getAmc().getId().equals(id))
                .map(entity -> {
                    SchemeDTO dto = new SchemeDTO();
                    BeanUtils.copyProperties(entity, dto);
                    if (entity.getAmc() != null) {
                        dto.setAmcId(entity.getAmc().getId());
                    }
                    return dto;
                }).toList();
        return schemeDTOs;
    }

    @Override
    public SchemeDTO getSchemeById(Long id) {
        Optional<SchemeEntity> optional = schemeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new com.maheshbhatt.financialnuggets.exception.AmcNonFoundException("Scheme not found with ID: " + id);
        }
        SchemeEntity entity = optional.get();
        SchemeDTO dto = new SchemeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getAmc() != null) {
            dto.setAmcId(entity.getAmc().getId());
        }
        return dto;
    }

    @Override
    public SchemeDTO getSchemeByCode(String schemeCode) {
        Optional<SchemeEntity> optional = schemeRepository.findBySchemeCode(schemeCode);
        if (optional.isEmpty()) {
            throw new com.maheshbhatt.financialnuggets.exception.AmcNonFoundException("Scheme not found with code: " + schemeCode);
        }
        SchemeEntity entity = optional.get();
        SchemeDTO dto = new SchemeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getAmc() != null) {
            dto.setAmcId(entity.getAmc().getId());
        }
        return dto;
    }

    @Override
    public SchemeDTO deleteSchemeById(Long id) {
        Optional<SchemeEntity> optional = schemeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new com.maheshbhatt.financialnuggets.exception.AmcNonFoundException("Scheme not found with ID: " + id);
        }
        SchemeEntity entity = optional.get();
        SchemeDTO dto = new SchemeDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getAmc() != null) {
            dto.setAmcId(entity.getAmc().getId());
        }
        schemeRepository.deleteById(id);
        return dto;
    }

    @Override
    public List<SchemeDTO> parseSchemeCsv(MultipartFile file) {
        try {
            List<String[]> csvRows = CsvReader.readCsv(file.getInputStream());
            //skip header row and process data
            List<SchemeDTO> schemeDTOs = csvRows.stream()
                    .skip(1)
//                    .map(row -> row.length > 1 && row[1] != null ? row[1].trim() : null)
//                    .filter(name -> name != null && !name.isEmpty())
//                    .distinct()
                    .map(row -> {
                        if (row == null || row.length < 7) {
                            return null;
                        }

                        String amcid = row[0] == null ? "" : row[0].trim();
                        if (amcid.isEmpty()) {
                            return null;
                        }
                        Optional<AmcEntity> amcOptional = amcRepostiory.findByAmcId(amcid);
                        if (amcOptional.isEmpty()) {
                            return null;
                        }
                        SchemeDTO schemeDTO = getSchemeDTO(row, amcOptional.get());
                        return schemeDTO;
                    })
                    .filter(Objects::nonNull)
                    .distinct().toList();


            return schemeDTOs.stream().map(this::save).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static SchemeDTO getSchemeDTO(String[] row, AmcEntity amcEntity) {
        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setAmcId(amcEntity.getId());
        schemeDTO.setSchemeCode(row[2] == null ? "" : row[2].trim());
        schemeDTO.setName(row[3] == null ? "" : row[3].trim());
        schemeDTO.setType(row[4] == null ? "" : row[4].trim());
        schemeDTO.setCategory(row[5] == null ? "" : row[5].trim());
        schemeDTO.setSchemeNavName(row[6] == null ? "" : row[6].trim());
        return schemeDTO;
    }
}
