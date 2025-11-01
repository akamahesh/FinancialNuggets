package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schemes")
public class SchemeController {

    @Autowired
    private SchemeService schemeService;

    @PostMapping
    public SchemeDTO save(
            @RequestBody SchemeDTO schemeDTO
    ) {
        return schemeService.save(schemeDTO);
    }

    @GetMapping
    public List<SchemeDTO> getAllSchemes() {
        return schemeService.getAllSchemes();
    }

    @GetMapping("/amc/{amcId}")
    public List<SchemeDTO> getAllSchemesByAmcId(
            @PathVariable Long amcId
    ) {
        return schemeService.getAllSchemesByAmcId(amcId);
    }

    @GetMapping("/{id}")
    public SchemeDTO getAllSchemesById(
            @PathVariable Long id
    ) {
        return schemeService.getSchemeById(id);
    }

    @DeleteMapping("/{id}")
    public SchemeDTO deleteSchemeById(
            @RequestParam Long id
    ) {
        return schemeService.deleteSchemeById(id);
    }


}
