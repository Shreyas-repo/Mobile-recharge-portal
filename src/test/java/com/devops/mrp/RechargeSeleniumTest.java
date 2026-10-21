package com.devops.mrp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RechargeSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*", "--window-size=1920,1080", "--disable-gpu");

        try {
            WebDriverManager.chromedriver().setup();
        } catch (Exception e) {
            System.out.println("[SELENIUM] WebDriverManager note: " + e.getMessage() + ". Relying on native Selenium 4 Manager.");
        }

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void safeClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
        try {
            Thread.sleep(200);
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void captureScreenshot(String testName) {
        if (driver instanceof TakesScreenshot takesScreenshot) {
            File src = takesScreenshot.getScreenshotAs(OutputType.FILE);
            try {
                Path destDir = Path.of("target", "screenshots");
                Files.createDirectories(destDir);
                Path dest = destDir.resolve(testName + "_" + System.currentTimeMillis() + ".png");
                Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[SELENIUM] Failure screenshot saved: " + dest.toAbsolutePath());
            } catch (IOException ioException) {
                System.err.println("[SELENIUM] Failed to save screenshot: " + ioException.getMessage());
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("UJ-01: User Journey - Browse Catalog & Filter Plans")
    void testPlanCatalogBrowsing() {
        try {
            driver.get(baseUrl + "/");
            assertTrue(driver.getTitle().contains("Mobile Recharge Portal"));

            // Check that plan cards are loaded
            List<WebElement> planCards = driver.findElements(By.className("plan-card"));
            assertFalse(planCards.isEmpty(), "Plans grid should render recharge plans");

            // Verify filter pill click (e.g. Jio)
            WebElement jioPill = driver.findElement(By.xpath("//a[contains(@class, 'operator-pill') and text()='Jio']"));
            assertNotNull(jioPill);
            safeClick(jioPill);

            wait.until(ExpectedConditions.urlContains("operator=Jio"));
            assertTrue(driver.getCurrentUrl().contains("operator=Jio"));
        } catch (Throwable t) {
            captureScreenshot("testPlanCatalogBrowsing_failed");
            throw t;
        }
    }

    @Test
    @Order(2)
    @DisplayName("UJ-02: User Journey - Select Plan, Submit Recharge & View Receipt")
    void testRechargeSubmissionAndReceipt() {
        try {
            driver.get(baseUrl + "/");

            // Click Choose Plan button on first plan
            List<WebElement> chooseButtons = driver.findElements(By.className("select-plan-btn"));
            assertFalse(chooseButtons.isEmpty(), "Choose plan buttons should be present");
            safeClick(chooseButtons.get(0));

            // Fill 10-digit mobile number
            WebElement mobileInput = driver.findElement(By.id("mobileNumber"));
            mobileInput.clear();
            mobileInput.sendKeys("9876543210");

            // Submit recharge
            WebElement submitBtn = driver.findElement(By.id("btn-submit-recharge"));
            safeClick(submitBtn);

            // Wait for receipt page redirect
            wait.until(ExpectedConditions.urlContains("/receipt/TXN-"));

            WebElement receiptStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("receipt-status")));
            assertNotNull(receiptStatus);
            assertEquals("SUCCESS", receiptStatus.getText().trim());

            WebElement receiptMobile = driver.findElement(By.id("receipt-mobile"));
            assertTrue(receiptMobile.getText().contains("9876543210"));

            WebElement receiptRef = driver.findElement(By.id("receipt-ref"));
            assertTrue(receiptRef.getText().startsWith("TXN-"));
        } catch (Throwable t) {
            captureScreenshot("testRechargeSubmissionAndReceipt_failed");
            throw t;
        }
    }

    @Test
    @Order(3)
    @DisplayName("UJ-03: User Journey - Search & Filter Transactions in Admin Ledger")
    void testAdminLedgerSearch() {
        try {
            driver.get(baseUrl + "/admin/transactions");

            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ledger-search")));
            searchInput.clear();
            searchInput.sendKeys("9876543210");

            WebElement searchBtn = driver.findElement(By.id("btn-search"));
            safeClick(searchBtn);

            wait.until(ExpectedConditions.urlContains("search=9876543210"));

            WebElement table = driver.findElement(By.id("transactions-table"));
            assertTrue(table.getText().contains("9876543210"), "Ledger should show transactions matching the searched mobile number");
        } catch (Throwable t) {
            captureScreenshot("testAdminLedgerSearch_failed");
            throw t;
        }
    }

    @Test
    @Order(4)
    @DisplayName("UJ-04: User Journey - Executive Analytics Dashboard Verification")
    void testAnalyticsDashboard() {
        try {
            driver.get(baseUrl + "/admin/dashboard");

            WebElement kpiTotalCount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("kpi-total-count")));
            assertNotNull(kpiTotalCount);
            int total = Integer.parseInt(kpiTotalCount.getText().trim());
            assertTrue(total > 0, "Dashboard KPI should report at least 1 recharge transaction");

            WebElement kpiRevenue = driver.findElement(By.id("kpi-total-revenue"));
            assertNotNull(kpiRevenue);
            assertFalse(kpiRevenue.getText().isBlank());

            WebElement kpiSuccessRate = driver.findElement(By.id("kpi-success-rate"));
            assertTrue(kpiSuccessRate.getText().contains("%"));
        } catch (Throwable t) {
            captureScreenshot("testAnalyticsDashboard_failed");
            throw t;
        }
    }
}
