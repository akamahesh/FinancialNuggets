package com.maheshbhatt.financialnuggets.controller.ui;

import com.maheshbhatt.financialnuggets.model.HoldingDTO;
import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.service.HoldingService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/schemes")
public class SchemeUIController {

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private HoldingService holdingService;

    @GetMapping
    public String listSchemes(Model model) {
        List<SchemeDTO> schemes = schemeService.getAllSchemes();
        model.addAttribute("schemes", schemes);
        return "scheme/list";
    }

    @GetMapping("/new")
    public String newSchemeForm(@RequestParam(required = false) Long amcId, Model model) {
        SchemeDTO scheme = new SchemeDTO();
        if (amcId != null) {
            scheme.setAmcId(amcId);
        }
        model.addAttribute("scheme", scheme);
        return "scheme/form";
    }

    @PostMapping
    public String createScheme(@ModelAttribute SchemeDTO schemeDTO, Model model) {
        SchemeDTO savedScheme = schemeService.save(schemeDTO);
        model.addAttribute("scheme", savedScheme);
        // If created from AMC context, redirect to AMC detail page
        if (savedScheme.getAmcId() != null) {
            return "redirect:/amcs/" + savedScheme.getAmcId();
        }
        // Return fragment for HTMX inline update, or redirect to detail page
        return "scheme/detail :: scheme-detail";
    }

    @GetMapping("/{id}")
    public String getSchemeDetail(@PathVariable Long id, Model model) {
        SchemeDTO scheme = schemeService.getSchemeById(id);
        List<HoldingDTO> holdings = holdingService.getHoldingsBySchemeCode(scheme.getSchemeCode());
        model.addAttribute("scheme", scheme);
        model.addAttribute("holdings", holdings);
        return "scheme/detail";
    }

    @DeleteMapping("/{id}")
    public String deleteScheme(@PathVariable Long id, Model model) {
        schemeService.deleteSchemeById(id);
        List<SchemeDTO> schemes = schemeService.getAllSchemes();
        model.addAttribute("schemes", schemes);
        return "scheme/list :: scheme-list";
    }

    @GetMapping("/partial/list")
    public String getSchemeListPartial(@RequestParam(required = false) Long amcId, Model model) {
        List<SchemeDTO> schemes;
        if (amcId != null) {
            schemes = schemeService.getAllSchemesByAmcId(amcId);
        } else {
            schemes = schemeService.getAllSchemes();
        }
        model.addAttribute("schemes", schemes);
        return "scheme/list :: scheme-list";
    }
}
