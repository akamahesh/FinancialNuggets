package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.model.AmcDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmcService {
    AmcDTO save(AmcDTO amcDTO);

    List<AmcDTO> getAllAmcs();

    AmcDTO getAmcById(Long id);

    AmcDTO deleteAmcById(Long id);

    String deleteAll();

    List<AmcDTO> parseAmcCsv(MultipartFile file);

}