package com.maheshbhatt.financialnuggets.controller.ui;

import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/holdings")
public class HoldingUIController {

    @Autowired
    private HoldingService holdingService;

    @Autowired
    private SchemeService schemeService;

    @GetMapping
    public String listHoldings(Model model) {
        List<HoldingDTO> holdings = holdingService.getAllHoldings();
        model.addAttribute("holdings", holdings);
        return "holding/list";
    }

    @GetMapping("/new")
    public String newHoldingForm(
            @RequestParam(required = false) Long amcId,
            @RequestParam(required = false) String schemeCode,
            Model model) {
        HoldingDTO holding = new HoldingDTO();
        if (amcId != null) {
            holding.setAmcId(amcId);
        }
        if (schemeCode != null) {
            holding.setSchemeCode(schemeCode);
        }
        model.addAttribute("holding", holding);
        return "holding/form";
    }

    @PostMapping
    public String createHolding(@ModelAttribute HoldingDTO holdingDTO, Model model) {
        HoldingDTO savedHolding = holdingService.save(holdingDTO);
        model.addAttribute("holding", savedHolding);
        // If created from scheme context, redirect to scheme detail page
        if (savedHolding.getSchemeCode() != null && !savedHolding.getSchemeCode().isEmpty()) {
            try {
                var scheme = schemeService.getSchemeByCode(savedHolding.getSchemeCode());
                return "redirect:/schemes/" + scheme.getId();
            } catch (Exception e) {
                // If scheme not found, just go to holdings list
            }
        }
        // Return fragment for HTMX inline update, or redirect to detail page
        return "holding/detail :: holding-detail";
    }

    @GetMapping("/{id}")
    public String getHoldingDetail(@PathVariable Long id, Model model) {
        HoldingDTO holding = holdingService.getHoldingById(id);
        model.addAttribute("holding", holding);
        return "holding/detail";
    }

    @PutMapping("/{id}")
    public String updateHolding(@PathVariable Long id, @ModelAttribute HoldingDTO holdingDTO, Model model) {
        HoldingDTO updatedHolding = holdingService.updateHolding(id, holdingDTO);
        model.addAttribute("holding", updatedHolding);
        return "holding/detail :: holding-detail";
    }

    @DeleteMapping("/{id}")
    public String deleteHolding(@PathVariable Long id, Model model) {
        holdingService.deleteHoldingById(id);
        List<HoldingDTO> holdings = holdingService.getAllHoldings();
        model.addAttribute("holdings", holdings);
        return "holding/list :: holding-list";
    }

    @GetMapping("/partial/list")
    public String getHoldingListPartial(
            @RequestParam(required = false) String schemeCode,
            Model model) {
        List<HoldingDTO> holdings;
        if (schemeCode != null) {
            holdings = holdingService.getHoldingsBySchemeCode(schemeCode);
        } else {
            holdings = holdingService.getAllHoldings();
        }
        model.addAttribute("holdings", holdings);
        return "holding/list :: holding-list";
    }

    @PostMapping(value = "/preview-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String previewCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Long amcId,
            @RequestParam(required = false) String schemeCode,
            Model model) {
        if (file == null || (!file.getOriginalFilename().endsWith(".csv") && !file.getContentType().equals("text/csv"))) {
            model.addAttribute("error", "Invalid file. Please upload a CSV file.");
            HoldingDTO holding = new HoldingDTO();
            if (amcId != null) {
                holding.setAmcId(amcId);
            }
            if (schemeCode != null) {
                holding.setSchemeCode(schemeCode);
            }
            model.addAttribute("holding", holding);
            return "holding/form";
        }

        try {
            List<HoldingDTO> previewHoldings = holdingService.previewHoldingCsv(file, amcId, schemeCode);
            model.addAttribute("previewHoldings", previewHoldings);
            model.addAttribute("showPreview", true);

            HoldingDTO holding = new HoldingDTO();
            if (amcId != null) {
                holding.setAmcId(amcId);
            }
            if (schemeCode != null) {
                holding.setSchemeCode(schemeCode);
            }
            model.addAttribute("holding", holding);
            model.addAttribute("csvFileName", file.getOriginalFilename());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse CSV: " + e.getMessage());
            HoldingDTO holding = new HoldingDTO();
            if (amcId != null) {
                holding.setAmcId(amcId);
            }
            if (schemeCode != null) {
                holding.setSchemeCode(schemeCode);
            }
            model.addAttribute("holding", holding);
        }

        return "holding/form";
    }

    @PostMapping("/bulk-save")
    public String bulkSaveHoldings(
            @RequestParam(required = false) Long amcId,
            @RequestParam(required = false) String schemeCode,
            @RequestParam(value = "holdings[].name", required = false) String[] names,
            @RequestParam(value = "holdings[].isin", required = false) String[] isins,
            @RequestParam(value = "holdings[].quantity", required = false) String[] quantities,
            @RequestParam(value = "holdings[].marketValue", required = false) String[] marketValues,
            @RequestParam(value = "holdings[].percentageOfAum", required = false) String[] percentageOfAums,
            @RequestParam(value = "holdings[].reportingDate", required = false) String[] reportingDates,
            Model model) {
        try {
            List<HoldingDTO> holdings = new java.util.ArrayList<>();
            if (names != null && names.length > 0) {
                for (int i = 0; i < names.length; i++) {
                    HoldingDTO holding = new HoldingDTO();
                    holding.setAmcId(amcId);
                    holding.setSchemeCode(schemeCode);
                    holding.setName(names[i]);
                    if (isins != null && i < isins.length && isins[i] != null && !isins[i].isEmpty()) {
                        holding.setIsin(isins[i]);
                    }
                    if (quantities != null && i < quantities.length && quantities[i] != null && !quantities[i].isEmpty()) {
                        try {
                            holding.setQuantity(Long.parseLong(quantities[i]));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    if (marketValues != null && i < marketValues.length && marketValues[i] != null && !marketValues[i].isEmpty()) {
                        try {
                            holding.setMarketValue(new java.math.BigDecimal(marketValues[i]));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    if (percentageOfAums != null && i < percentageOfAums.length && percentageOfAums[i] != null && !percentageOfAums[i].isEmpty()) {
                        try {
                            holding.setPercentageOfAum(Long.parseLong(percentageOfAums[i]));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    if (reportingDates != null && i < reportingDates.length && reportingDates[i] != null && !reportingDates[i].isEmpty()) {
                        holding.setReportingDate(reportingDates[i]);
                    }
                    holdings.add(holding);
                }
            }

            List<HoldingDTO> savedHoldings = holdingService.saveAll(holdings);
            model.addAttribute("message", "Successfully saved " + savedHoldings.size() + " holdings");

            // Redirect to scheme detail if schemeCode is provided
            if (schemeCode != null && !schemeCode.isEmpty()) {
                try {
                    var scheme = schemeService.getSchemeByCode(schemeCode);
                    return "redirect:/schemes/" + scheme.getId();
                } catch (Exception e) {
                    // If scheme not found, go to holdings list
                }
            }

            return "redirect:/holdings";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save holdings: " + e.getMessage());
            HoldingDTO holding = new HoldingDTO();
            if (amcId != null) {
                holding.setAmcId(amcId);
            }
            if (schemeCode != null) {
                holding.setSchemeCode(schemeCode);
            }
            model.addAttribute("holding", holding);
            return "holding/form";
        }
    }
}
