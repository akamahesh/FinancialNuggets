package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.model.AmcRequestDTO;
import com.maheshbhatt.financialnuggets.model.AmcResponseDTO;

import java.util.List;

public interface AmcService {
    AmcResponseDTO save(AmcRequestDTO amcRequestDTO);

    List<AmcResponseDTO> getAllAmcs();

    AmcResponseDTO getAmcById(Long id);

    AmcResponseDTO deleteAmcById(Long id);
}
