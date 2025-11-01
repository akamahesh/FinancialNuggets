package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.AmcEntity;
import com.maheshbhatt.financialnuggets.model.AmcRequestDTO;
import com.maheshbhatt.financialnuggets.model.AmcResponseDTO;
import com.maheshbhatt.financialnuggets.repository.AmcRepostiory;
import com.maheshbhatt.financialnuggets.service.AmcService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmcServiceImpl implements AmcService {


    @Autowired
    private AmcRepostiory amcRepostiory;

    @Override
    public AmcResponseDTO save(AmcRequestDTO amcRequestDTO) {
        AmcEntity entity = new AmcEntity();
        BeanUtils.copyProperties(amcRequestDTO, entity);
        AmcEntity saved = amcRepostiory.save(entity);
        AmcResponseDTO responseDTO = new AmcResponseDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        return responseDTO;
    }

    @Override
    public List<AmcResponseDTO> getAllAmcs() {
        List<AmcEntity> amcEntities = amcRepostiory.findAll();
        List<AmcResponseDTO> amcResponse = amcEntities.stream().map(entity -> {
            AmcResponseDTO amc = new AmcResponseDTO();
            BeanUtils.copyProperties(entity, amc);
            return amc;
        }).toList();
        return amcResponse;
    }

    @Override
    public AmcResponseDTO getAmcById(String amcId) {
        return null;
    }

    @Override
    public String deleteAmcById(String amcId) {
        return "";
    }
}
