package com.devops.mrp.service;

import com.devops.mrp.model.RechargePlan;
import com.devops.mrp.model.RechargeTransaction;
import com.devops.mrp.repository.RechargePlanRepository;
import com.devops.mrp.repository.RechargeTransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RechargeService {

    private final RechargePlanRepository planRepository;
    private final RechargeTransactionRepository transactionRepository;
    private final SecureRandom random = new SecureRandom();

    public RechargeService(RechargePlanRepository planRepository, RechargeTransactionRepository transactionRepository) {
        this.planRepository = planRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    public void seedInitialData() {
        if (planRepository.count() == 0) {
            List<RechargePlan> samplePlans = List.of(
                // Airtel
                new RechargePlan("Airtel", "Truly Unlimited 5G", "Unlimited", 299.0, 28, "1.5 GB/day", "Unlimited Calls + 100 SMS/day + Airtel Xstream"),
                new RechargePlan("Airtel", "Hero Unlimited Pro", "Unlimited", 479.0, 56, "1.5 GB/day", "Unlimited Voice + Apollo 24/7 + Free Hellotunes"),
                new RechargePlan("Airtel", "Super Data Booster", "Data", 65.0, 1, "4 GB Bulk", "High Speed Data valid with existing pack"),
                new RechargePlan("Airtel", "Annual Entertainment", "Popular", 2999.0, 365, "2.0 GB/day", "Unlimited Calls + Disney+ Hotstar 1 Year"),

                // Jio
                new RechargePlan("Jio", "Jio True 5G Prime", "Popular", 239.0, 28, "1.5 GB/day", "Unlimited 5G Data + JioCinema & JioTV"),
                new RechargePlan("Jio", "Jio Freedom 84 Days", "Unlimited", 666.0, 84, "1.5 GB/day", "Unlimited Calls + 100 SMS/day + JioCloud 50GB"),
                new RechargePlan("Jio", "Cricket Data Add-on", "Data", 121.0, 30, "12 GB High Speed", "Work from Home / Streaming Booster"),
                new RechargePlan("Jio", "Top-Up Talktime", "Talktime", 100.0, 365, "N/A", "Full Talktime ₹81.75 + Free outgoing benefits"),

                // Vi (Vodafone Idea)
                new RechargePlan("Vi", "Hero Unlimited Binge", "Popular", 299.0, 28, "1.5 GB/day", "Binge All Night (12am-6am) + Weekend Rollover"),
                new RechargePlan("Vi", "Quarterly Delight", "Unlimited", 719.0, 84, "1.5 GB/day", "Unlimited Calls + Data Delight up to 2GB extra/month"),
                new RechargePlan("Vi", "Work From Home Pack", "Data", 118.0, 28, "12 GB Bulk", "Pure Data pack for continuous internet connectivity"),

                // BSNL
                new RechargePlan("BSNL", "Voice Special Vouchers", "Talktime", 107.0, 35, "3 GB Total", "200 Mins Voice calls + BSNL Tunes"),
                new RechargePlan("BSNL", "Bharat Fiber Connect", "Unlimited", 397.0, 150, "2 GB/day for 30d", "Extended Validity Pack with free calling")
            );
            planRepository.saveAll(samplePlans);
        }

        if (transactionRepository.count() == 0) {
            List<RechargeTransaction> seedTxns = List.of(
                new RechargeTransaction("TXN-20261021-1001", "9876543210", "Airtel", 299.0, "Truly Unlimited 5G", "SUCCESS", "Automated gateway settlement"),
                new RechargeTransaction("TXN-20261021-1002", "9811223344", "Jio", 666.0, "Jio Freedom 84 Days", "SUCCESS", "Fast 5G recharge activated"),
                new RechargeTransaction("TXN-20261021-1003", "9722334455", "Vi", 299.0, "Hero Unlimited Binge", "PENDING", "Waiting for telecom provider ACK"),
                new RechargeTransaction("TXN-20261021-1004", "9633445566", "BSNL", 107.0, "Voice Special Vouchers", "FAILED", "Subscriber circle timeout")
            );
            transactionRepository.saveAll(seedTxns);
        }
    }

    public List<RechargePlan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<RechargePlan> getPlansByOperator(String operator) {
        if (operator == null || operator.isBlank() || operator.equalsIgnoreCase("All")) {
            return planRepository.findAll();
        }
        return planRepository.findByOperatorIgnoreCase(operator);
    }

    public Optional<RechargePlan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    @Transactional
    public RechargeTransaction initiateRecharge(String mobileNumber, String operator, Long planId, Double customAmount) {
        String planName = "Custom Top-up";
        Double amount = customAmount;

        if (planId != null && planId > 0) {
            Optional<RechargePlan> planOpt = planRepository.findById(planId);
            if (planOpt.isPresent()) {
                RechargePlan plan = planOpt.get();
                planName = plan.getPlanName() + " (" + plan.getValidityDays() + " Days)";
                amount = plan.getAmount();
                if (operator == null || operator.isBlank()) {
                    operator = plan.getOperator();
                }
            }
        }

        if (amount == null || amount <= 0) {
            amount = 199.0;
        }

        String txnReference = "TXN-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (1000 + random.nextInt(9000));
        RechargeTransaction txn = new RechargeTransaction(txnReference, mobileNumber, operator, amount, planName, "SUCCESS", "Direct online recharge processed");
        return transactionRepository.save(txn);
    }

    public List<RechargeTransaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<RechargeTransaction> getTransactionByReference(String ref) {
        return transactionRepository.findByTxnReference(ref);
    }

    public List<RechargeTransaction> searchTransactions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTransactions();
        }
        return transactionRepository.searchTransactions(query.trim());
    }

    @Transactional
    public boolean updateTransactionStatus(Long id, String newStatus, String notes) {
        Optional<RechargeTransaction> txnOpt = transactionRepository.findById(id);
        if (txnOpt.isPresent()) {
            RechargeTransaction txn = txnOpt.get();
            txn.setStatus(newStatus.toUpperCase());
            if (notes != null && !notes.isBlank()) {
                txn.setNotes(notes);
            }
            transactionRepository.save(txn);
            return true;
        }
        return false;
    }

    public Map<String, Object> getDashboardMetrics() {
        List<RechargeTransaction> all = transactionRepository.findAll();
        long totalCount = all.size();
        double totalRevenue = all.stream().mapToDouble(RechargeTransaction::getAmount).sum();
        long successCount = all.stream().filter(t -> "SUCCESS".equalsIgnoreCase(t.getStatus())).count();
        long pendingCount = all.stream().filter(t -> "PENDING".equalsIgnoreCase(t.getStatus())).count();
        long failedCount = all.stream().filter(t -> "FAILED".equalsIgnoreCase(t.getStatus())).count();

        double successRate = totalCount > 0 ? (double) successCount / totalCount * 100.0 : 0.0;

        Map<String, Long> operatorBreakdown = new HashMap<>();
        for (RechargeTransaction t : all) {
            operatorBreakdown.put(t.getOperator(), operatorBreakdown.getOrDefault(t.getOperator(), 0L) + 1);
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalCount", totalCount);
        metrics.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        metrics.put("successCount", successCount);
        metrics.put("pendingCount", pendingCount);
        metrics.put("failedCount", failedCount);
        metrics.put("successRate", Math.round(successRate * 10.0) / 10.0);
        metrics.put("operatorBreakdown", operatorBreakdown);

        return metrics;
    }
}
