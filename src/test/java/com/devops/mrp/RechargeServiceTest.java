package com.devops.mrp;

import com.devops.mrp.model.RechargePlan;
import com.devops.mrp.model.RechargeTransaction;
import com.devops.mrp.service.RechargeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RechargeServiceTest {

    @Autowired
    private RechargeService rechargeService;

    @Test
    @DisplayName("UT-01: Plan Catalog should seed initial plans")
    void testPlanCatalogSeeding() {
        List<RechargePlan> plans = rechargeService.getAllPlans();
        assertNotNull(plans);
        assertTrue(plans.size() >= 10, "Should have at least 10 seeded plans across operators");
    }

    @Test
    @DisplayName("UT-02: Filter plans by operator should return only matching operator")
    void testFilterByOperator() {
        List<RechargePlan> airtelPlans = rechargeService.getPlansByOperator("Airtel");
        assertFalse(airtelPlans.isEmpty());
        assertTrue(airtelPlans.stream().allMatch(p -> "Airtel".equalsIgnoreCase(p.getOperator())));
    }

    @Test
    @DisplayName("UT-03: Initiate recharge creates valid transaction with TXN reference")
    void testInitiateRecharge() {
        String testMobile = "9876543210";
        RechargeTransaction txn = rechargeService.initiateRecharge(testMobile, "Airtel", 1L, 299.0);
        
        assertNotNull(txn);
        assertNotNull(txn.getId());
        assertTrue(txn.getTxnReference().startsWith("TXN-"));
        assertEquals(testMobile, txn.getMobileNumber());
        assertEquals("SUCCESS", txn.getStatus());
    }

    @Test
    @DisplayName("UT-04: Update transaction status changes status successfully")
    void testUpdateTransactionStatus() {
        // Create a pending transaction
        RechargeTransaction txn = rechargeService.initiateRecharge("9988776655", "Jio", null, 199.0);
        rechargeService.updateTransactionStatus(txn.getId(), "PENDING", "Initial mock hold");

        // Now transition to SUCCESS
        boolean updated = rechargeService.updateTransactionStatus(txn.getId(), "SUCCESS", "Manual audit approval");
        assertTrue(updated);

        Optional<RechargeTransaction> verified = rechargeService.getTransactionByReference(txn.getTxnReference());
        assertTrue(verified.isPresent());
        assertEquals("SUCCESS", verified.get().getStatus());
    }

    @Test
    @DisplayName("UT-05: Dashboard metrics calculate correct totals and revenue")
    void testDashboardMetrics() {
        Map<String, Object> metrics = rechargeService.getDashboardMetrics();
        assertNotNull(metrics);
        assertTrue((Long) metrics.get("totalCount") > 0);
        assertTrue((Double) metrics.get("totalRevenue") > 0);
        assertTrue((Double) metrics.get("successRate") >= 0);
    }
}
