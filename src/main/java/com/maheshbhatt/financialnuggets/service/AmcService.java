package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.entity.AmcEntity;
import com.maheshbhatt.financialnuggets.model.AmcRequestDTO;
import com.maheshbhatt.financialnuggets.model.AmcResponseDTO;

import java.util.List;

public interface AmcService {
    AmcResponseDTO save(AmcRequestDTO amcRequestDTO);
    List<AmcResponseDTO> getAllAmcs();
    AmcResponseDTO getAmcById(String amcId);
    String deleteAmcById(String amcId);
}
