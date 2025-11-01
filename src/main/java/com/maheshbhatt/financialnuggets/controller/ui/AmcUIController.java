package com.maheshbhatt.financialnuggets.controller.ui;

import com.maheshbhatt.financialnuggets.model.AmcRequestDTO;
import com.maheshbhatt.financialnuggets.model.AmcResponseDTO;
import com.maheshbhatt.financialnuggets.model.SchemeDTO;
import com.maheshbhatt.financialnuggets.service.AmcService;
import com.maheshbhatt.financialnuggets.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/amcs")
public class AmcUIController {

    @Autowired
    private AmcService amcService;

    @Autowired
    private SchemeService schemeService;

    @GetMapping
    public String listAmcs(Model model) {
        List<AmcResponseDTO> amcs = amcService.getAllAmcs();
        model.addAttribute("amcs", amcs);
        return "amc/list";
    }

    @GetMapping("/new")
    public String newAmcForm(Model model) {
        model.addAttribute("amc", new AmcRequestDTO());
        return "amc/form";
    }

    @PostMapping
    public String createAmc(@ModelAttribute AmcRequestDTO amcRequestDTO, Model model) {
        AmcResponseDTO savedAmc = amcService.save(amcRequestDTO);
        model.addAttribute("amc", savedAmc);
        // Return fragment for HTMX inline update, or redirect to detail page
        return "amc/detail :: amc-detail";
    }

    @GetMapping("/{id}")
    public String getAmcDetail(@PathVariable Long id, Model model) {
        AmcResponseDTO amc = amcService.getAmcById(id);
        List<SchemeDTO> schemes = schemeService.getAllSchemesByAmcId(id);
        model.addAttribute("amc", amc);
        model.addAttribute("schemes", schemes);
        return "amc/detail";
    }

    @DeleteMapping("/{id}")
    public String deleteAmc(@PathVariable Long id, Model model) {
        amcService.deleteAmcById(id);
        List<AmcResponseDTO> amcs = amcService.getAllAmcs();
        model.addAttribute("amcs", amcs);
        return "amc/list :: amc-list";
    }

    @GetMapping("/partial/list")
    public String getAmcListPartial(Model model) {
        List<AmcResponseDTO> amcs = amcService.getAllAmcs();
        model.addAttribute("amcs", amcs);
        return "amc/list :: amc-list";
    }
}

