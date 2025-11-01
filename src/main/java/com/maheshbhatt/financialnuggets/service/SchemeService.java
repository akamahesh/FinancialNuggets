package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SchemeService {

    SchemeDTO save(SchemeDTO schemeDTO);

    List<SchemeDTO> getAllSchemes();

    List<SchemeDTO> getAllSchemesByAmcId(Long id);

    SchemeDTO getSchemeById(Long id);
    
    SchemeDTO getSchemeByCode(String schemeCode);

    SchemeDTO deleteSchemeById(Long id);

    List<SchemeDTO> parseSchemeCsv(MultipartFile file);
}
