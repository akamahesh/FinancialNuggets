package com.maheshbhatt.financialnuggets.controller.seeder;

import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.service.AmcService;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sanitize")
public class SantizerController {

    @Autowired
    private AmcService amcService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private HoldingService holdingService;


    @PostMapping("/amcs")
    public String sanitizeAmcs() {
        return "Sanitize AMC data - Not Implemented Yet";
    }

    @PostMapping("/holdings")
    public String sanitizeHoldings() {
        return "Sanitized Holdings data " + holdingService.deleteAll();
    }

    @GetMapping("/schemes")
    public List<SchemeDTO> sanitizeSchemes() {

        HashSet<String> schemeName = new HashSet<>();
        List<SchemeDTO> schemeDTOList = schemeService.getAllSchemes();
        // remove all duplicate schemes based on scheme name. and amcid.
        val distinctSchemes = schemeDTOList.stream()
                .filter(dto -> schemeName.add(dto.getName() + "_" + dto.getAmcId()))
                .toList();

        //clear existing schemes and re insert the distinct schemes.
        schemeService.deleteAllSchemes();
        distinctSchemes.forEach(schemeDTO -> {
            schemeService.save(schemeDTO);
        });
        return distinctSchemes;
    }
}
