package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.AmcRequestDTO;
import com.maheshbhatt.financialnuggets.model.AmcResponseDTO;
import com.maheshbhatt.financialnuggets.service.AmcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/amcs")
public class AmcController {

    @Autowired
    private AmcService amcService;

    @PostMapping
    public AmcResponseDTO save(@RequestBody AmcRequestDTO amcRequestDTO) {
        return amcService.save(amcRequestDTO);
    }

    @GetMapping
    public List<AmcResponseDTO> getAllAmcs() {
        return amcService.getAllAmcs();
    }


}
