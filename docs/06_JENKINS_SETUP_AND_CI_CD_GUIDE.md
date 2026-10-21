# 🚀 Phase 7 & 8: Jenkins CI/CD Installation & Execution Guide
## Mobile Recharge Portal (MRP)

---

### 1. Zero-Hassle Jenkins Setup on Windows
You can run Jenkins on your machine without complicated enterprise setup using the standalone official WAR:

#### Option A: Run Jenkins via Java (Recommended & Instant)
1. Download `jenkins.war` (LTS) into `c:\tools\jenkins\`:
   ```powershell
   New-Item -ItemType Directory -Force -Path 'c:\tools\jenkins'
   Invoke-WebRequest -Uri 'https://get.jenkins.io/war-stable/latest/jenkins.war' -OutFile 'c:\tools\jenkins\jenkins.war'
   ```
2. Start Jenkins on port `9090` (to avoid collision with app port `8080`):
   ```powershell
   java -jar c:\tools\jenkins\jenkins.war --httpPort=9090
   ```
3. Open `http://localhost:9090` in your browser.
4. Unlock Jenkins using the initial administrator password displayed in the terminal console (or located in `C:\Users\Admin\.jenkins\secrets\initialAdminPassword`).
5. Click **"Install Suggested Plugins"** (installs Git, Pipeline, JUnit, Workspace Cleanup).

---

### 2. Configure Global Tools in Jenkins
Navigate to: **Manage Jenkins $\rightarrow$ Tools**:
1. **JDK**:
   - Name: `JDK-21`
   - JAVA_HOME: `C:\Program Files\Java\jdk-21.0.12.1`
2. **Maven**:
   - Name: `Maven-3.9.6`
   - MAVEN_HOME: `C:\tools\apache-maven-3.9.6`

---

### 3. Create the CI/CD Pipeline Job
1. From Jenkins Dashboard, click **New Item**.
2. Enter Name: `Mobile-Recharge-Portal-Pipeline`.
3. Select **Pipeline** and click **OK**.
4. In the Job Configuration page:
   - Check **"This project is parameterized"** (if not already picked up by Jenkinsfile).
   - Under **Build Triggers**: Check **"Poll SCM"** with schedule `H/5 * * * *` (polls every 5 mins) or **"GitHub hook trigger for GITScm polling"**.
   - Under **Pipeline**:
     - Definition: **Pipeline script from SCM**
     - SCM: **Git**
     - Repository URL: Path to local repo (`file:///C:/Users/Admin/Desktop/Devops_project`) or your GitHub URL (`https://github.com/<your-username>/mobile-recharge-portal.git`).
     - Branch Specifier: `*/main` or `*/develop`
     - Script Path: `Jenkinsfile`
5. Click **Save**.

---

### 4. Running the Pipeline & Verifying Stages
1. Click **"Build with Parameters"** $\rightarrow$ Click **"Build"**.
2. Inspect the Stage View:
   - ✅ Stage 1: Checkout SCM
   - ✅ Stage 2: Build & Unit Tests (`mvn clean test -Dtest=RechargeServiceTest`)
   - ✅ Stage 3: Selenium E2E Tests (`mvn test -Dtest=RechargeSeleniumTest`)
   - ✅ Stage 4: Package Artifact (`mobile-recharge-portal-1.0.0-SNAPSHOT.jar`)
   - ✅ Stage 5: Archive Artifacts (JAR + JUnit XML + Screenshots)
3. Under Build Status, verify:
   - **Test Result Trend**: View green JUnit test execution graph.
   - **Build Artifacts**: Download the archived `.jar` file directly.

---

### 5. Continuous Testing Failure & Correction Demonstration (Phase 10)
To demonstrate the quality gate stopping deployment:
1. **Deliberate Defect Injection**:
   - In `RechargeWebController.java`, change line 33 to invalidate 10-digit check:
     `if (mobileNumber == null || !mobileNumber.matches("^000$"))`
2. **Commit & Push**:
   - Commit: `git commit -am "test(defect): introduce deliberate validation defect"`
3. **Pipeline Execution**:
   - Jenkins runs Stage 3 (Selenium E2E Tests).
   - `testRechargeSubmissionAndReceipt` fails!
   - Pipeline immediately halts at Stage 3 with red status and **does not proceed to deployment**.
   - Failure screenshot is archived in `target/screenshots/`.
4. **Correction & Success**:
   - Revert change: `git checkout HEAD~1 -- src/main/java/com/devops/mrp/controller/RechargeWebController.java`
   - Commit: `git commit -am "fix(validation): restore 10-digit regex validation"`
   - Jenkins builds again: Green checkmark!
