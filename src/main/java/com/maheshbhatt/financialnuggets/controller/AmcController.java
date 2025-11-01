package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.AmcDTO;
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
    public AmcDTO save(@RequestBody AmcDTO amcDTO) {
        return amcService.save(amcDTO);
    }

    @GetMapping
    public List<AmcDTO> getAllAmcs() {
        return amcService.getAllAmcs();
    }

    @GetMapping("/{id}")
    public AmcDTO getAmcById(@PathVariable Long id) {
        return amcService.getAmcById(id);
    }

    @DeleteMapping("/{id}")
    public AmcDTO deleteAmcById(@PathVariable Long id) {
        return amcService.deleteAmcById(id);
    }

    @DeleteMapping("/all")
    public String deleteAll() {
        return amcService.deleteAll();
    }


}
