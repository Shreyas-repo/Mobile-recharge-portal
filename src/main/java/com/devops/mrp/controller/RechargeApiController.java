package com.devops.mrp.controller;

import com.devops.mrp.model.RechargePlan;
import com.devops.mrp.model.RechargeTransaction;
import com.devops.mrp.service.RechargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RechargeApiController {

    private final RechargeService rechargeService;

    public RechargeApiController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "Mobile Recharge Portal");
        health.put("version", "1.0.0-MVP");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<RechargePlan>> getAllPlans(@RequestParam(required = false) String operator) {
        return ResponseEntity.ok(rechargeService.getPlansByOperator(operator));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<RechargeTransaction>> getAllTransactions() {
        return ResponseEntity.ok(rechargeService.getAllTransactions());
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(rechargeService.getDashboardMetrics());
    }
}
