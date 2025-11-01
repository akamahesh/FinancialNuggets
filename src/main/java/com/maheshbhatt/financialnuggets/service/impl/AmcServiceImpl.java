package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.AmcEntity;
import com.maheshbhatt.financialnuggets.exception.AmcNonFoundException;
import com.maheshbhatt.financialnuggets.model.AmcDTO;
import com.maheshbhatt.financialnuggets.repository.AmcRepostiory;
import com.maheshbhatt.financialnuggets.service.AmcService;
import com.maheshbhatt.financialnuggets.utils.CsvReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AmcServiceImpl implements AmcService {

    final Set<String> amcIdSet = new HashSet<>();

    @Autowired
    private AmcRepostiory amcRepostiory;

    @Override
    public AmcDTO save(AmcDTO amcDTO) {
        AmcEntity entity = new AmcEntity();
        BeanUtils.copyProperties(amcDTO, entity);
        AmcEntity saved = amcRepostiory.save(entity);
        AmcDTO responseDTO = new AmcDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        return responseDTO;
    }

    @Override
    public List<AmcDTO> getAllAmcs() {
        List<AmcEntity> amcEntities = amcRepostiory.findAll();
        List<AmcDTO> amcResponse = amcEntities.stream().map(entity -> {
            AmcDTO amc = new AmcDTO();
            BeanUtils.copyProperties(entity, amc);
            return amc;
        }).toList();
        return amcResponse;
    }

    @Override
    public AmcDTO getAmcById(Long id) {
        return amcRepostiory.findById(id)
                .map(entity -> {
                    AmcDTO dto = new AmcDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .orElseThrow(() -> new AmcNonFoundException("Amc not found with ID: " + id));
    }

    @Override
    public AmcDTO deleteAmcById(Long id) {
        AmcEntity amcEntity = amcRepostiory.findById(id)
                .orElseThrow(() -> new AmcNonFoundException("Amc not found with ID: " + id));
        AmcDTO dto = new AmcDTO();
        BeanUtils.copyProperties(amcEntity, dto);
        amcRepostiory.deleteById(dto.getId());
        return dto;
    }

    @Override
    public String deleteAll() {
        amcRepostiory.deleteAll();
        return "All Deleted Successfully";
    }

    @Override
    public List<AmcDTO> parseAmcCsv(MultipartFile file) {
        try {
            List<String[]> csvRows = CsvReader.readCsv(file.getInputStream());
            //skip header row and process data
            List<AmcDTO> amcDTOS = csvRows.stream()
                    .skip(1)
                    .filter(row -> amcIdSet.add(row[0].trim()))
                    .distinct()
                    .map(row -> {
                        AmcDTO amcDTO = new AmcDTO();
                        amcDTO.setAmcId(row[0].trim());
                        amcDTO.setAmcName(row[1].trim());
                        amcDTO.setShortName(row[1].trim());
                        return save(amcDTO);
                    })
                    .toList();

            return amcDTOS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
