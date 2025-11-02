package com.maheshbhatt.financialnuggets.controller.ui;

import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import com.maheshbhatt.financialnuggets.utils.DateParser;
import com.maheshbhatt.financialnuggets.utils.MoneyUtils;
import com.maheshbhatt.financialnuggets.utils.PercentageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

}
