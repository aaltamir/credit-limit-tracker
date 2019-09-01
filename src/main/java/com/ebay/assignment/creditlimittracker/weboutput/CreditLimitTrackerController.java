package com.ebay.assignment.creditlimittracker.weboutput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class CreditLimitTrackerController {

    private final CreditLimitTrackerService creditLimitTrackerService;

    @RequestMapping(value = "/")
    public String index(final Model model) {
        model.addAttribute("creditLimits", creditLimitTrackerService.getCreditLimitInformation());
        return "index";
    }
}
