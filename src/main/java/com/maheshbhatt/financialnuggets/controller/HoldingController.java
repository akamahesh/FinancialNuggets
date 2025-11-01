package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/holdings")
public class HoldingController {

    @Autowired
    private HoldingService holdingService;

    @PostMapping
    public HoldingDTO save(@RequestBody HoldingDTO holdingDTO) {
        return holdingService.save(holdingDTO);
    }

    @GetMapping
    public List<HoldingDTO> getAllHoldings() {
        return holdingService.getAllHoldings();
    }

    @GetMapping("/{id}")
    public HoldingDTO getHoldingById(@PathVariable Long id) {
        return holdingService.getHoldingById(id);
    }

    @PutMapping("/{id}")
    public HoldingDTO updateHolding(@PathVariable Long id, @RequestBody HoldingDTO holdingDTO) {
        return holdingService.updateHolding(id, holdingDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteHoldingById(@PathVariable Long id) {
        holdingService.deleteHoldingById(id);
    }

    @GetMapping("/amc/{amcId}")
    public List<HoldingDTO> getHoldingsByAmcId(@PathVariable Long amcId) {
        return holdingService.getHoldingsByAmcId(amcId);
    }
    @GetMapping("/scheme/{schemeCode}")
    public List<HoldingDTO> getHoldingsBySchemeCode(@PathVariable String schemeCode) {
        return holdingService.getHoldingsBySchemeCode(schemeCode);
    }

    @GetMapping("/amc/{amcId}/scheme/{schemeCode}")
    public List<HoldingDTO> getHoldingsByAmcIdAndSchemeCode(
            @PathVariable Long amcId,
            @PathVariable String schemeCode) {
        return holdingService.getHoldingsByAmcIdAndSchemeCode(amcId, schemeCode);
    }

    @GetMapping("/reporting-date/{reportingDate}")
    public List<HoldingDTO> getHoldingsByReportingDate(@PathVariable LocalDate reportingDate) {
        return holdingService.getHoldingsByReportingDate(reportingDate);
    }

}
