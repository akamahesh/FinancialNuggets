package com.maheshbhatt.financialnuggets.controller.seeder;

import com.maheshbhatt.financialnuggets.model.AmcDTO;
import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.service.AmcService;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seed")
public class DataSeederController {

    @Autowired
    private AmcService amcService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private HoldingService holdingService;

    @PostMapping(value = "/amcs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AmcDTO> seedAmcs(@RequestPart("file") MultipartFile file) {
        if (file == null || !file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Invalid file. Please upload a CSV file.");
        }
        List<AmcDTO> amcDTOS = amcService.parseAmcCsv(file);
        return amcDTOS;
    }

    @PostMapping(value = "/schemes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<SchemeDTO> seedSchemes(@RequestPart("file") MultipartFile file) {
        if (file == null || !file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Invalid file. Please upload a CSV file.");
        }
        List<SchemeDTO> schemeDTOS = schemeService.parseSchemeCsv(file);
        return schemeDTOS;
    }




}
