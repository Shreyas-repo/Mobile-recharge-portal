package com.devops.mrp.controller;

import com.devops.mrp.model.RechargePlan;
import com.devops.mrp.model.RechargeTransaction;
import com.devops.mrp.service.RechargeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class RechargeWebController {

    private final RechargeService rechargeService;

    public RechargeWebController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "operator", required = false, defaultValue = "All") String operator,
                        @RequestParam(name = "selectedPlanId", required = false) Long selectedPlanId,
                        Model model) {
        List<RechargePlan> plans = rechargeService.getPlansByOperator(operator);
        model.addAttribute("plans", plans);
        model.addAttribute("currentOperator", operator);
        model.addAttribute("selectedPlanId", selectedPlanId);
        return "index";
    }

    @PostMapping("/recharge")
    public String processRecharge(@RequestParam("mobileNumber") String mobileNumber,
                                  @RequestParam(value = "operator", required = false) String operator,
                                  @RequestParam(value = "planId", required = false) Long planId,
                                  @RequestParam(value = "customAmount", required = false) Double customAmount,
                                  RedirectAttributes redirectAttributes) {
        // Validation: 10 digit number
        if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid Mobile Number! Please enter a valid 10-digit mobile number.");
            return "redirect:/";
        }

        RechargeTransaction txn = rechargeService.initiateRecharge(mobileNumber, operator, planId, customAmount);
        redirectAttributes.addFlashAttribute("successMessage", "Recharge successful! Ref: " + txn.getTxnReference());
        return "redirect:/receipt/" + txn.getTxnReference();
    }

    @GetMapping("/receipt/{txnReference}")
    public String viewReceipt(@PathVariable("txnReference") String txnReference, Model model) {
        Optional<RechargeTransaction> txnOpt = rechargeService.getTransactionByReference(txnReference);
        if (txnOpt.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("txn", txnOpt.get());
        return "receipt";
    }

    @GetMapping("/admin/transactions")
    public String adminLedger(@RequestParam(name = "search", required = false) String search, Model model) {
        List<RechargeTransaction> transactions = rechargeService.searchTransactions(search);
        model.addAttribute("transactions", transactions);
        model.addAttribute("searchQuery", search);
        return "admin_transactions";
    }

    @PostMapping("/admin/transactions/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status,
                               @RequestParam(value = "notes", required = false) String notes,
                               RedirectAttributes redirectAttributes) {
        boolean updated = rechargeService.updateTransactionStatus(id, status, notes);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Transaction #" + id + " status changed to " + status);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction #" + id + " not found!");
        }
        return "redirect:/admin/transactions";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("metrics", rechargeService.getDashboardMetrics());
        return "admin_dashboard";
    }
}
