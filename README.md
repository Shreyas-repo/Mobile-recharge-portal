# 📱 PayPulse: Mobile Recharge Portal (MRP)
### Enterprise End-to-End DevOps Lifecycle Implementation

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-red.svg?logo=apachemaven)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg?logo=docker)](https://www.docker.com/)
[![Selenium](https://img.shields.io/badge/Selenium-4.20.0-green.svg?logo=selenium)](https://www.selenium.dev/)
[![Jenkins](https://img.shields.io/badge/Jenkins-Pipeline_as_Code-red.svg?logo=jenkins)](https://www.jenkins.io/)
[![Ansible](https://img.shields.io/badge/Ansible-Playbook_Ready-black.svg?logo=ansible)](https://www.ansible.com/)

---

## 📌 Executive Summary
**PayPulse Mobile Recharge Portal (MRP)** is a full-stack, enterprise-grade telecommunications recharge and transaction auditing platform engineered to demonstrate an end-to-end DevOps lifecycle from real-time problem scoping to automated provisioning.

The system features:
- **Instant Plan Discovery**: Filter plans across India's premier operators (Airtel, Jio, Vi, BSNL).
- **Fast Recharge Checkout**: Strict 10-digit number validation with instantaneous transaction reference generation.
- **Digital Invoice / Receipt**: Printable confirmation with unique reference code (`TXN-YYYYMMDD-XXXX`).
- **Real-Time Admin Ledger**: Searchable, filterable audit log with role-based status transition workflow (`PENDING` $\rightarrow$ `SUCCESS` / `FAILED`).
- **Executive Analytics Dashboard**: Real-time KPI statistics (Recharges Volume, Gross Revenue, Success Rate, Network Distribution).

---

## 🏗️ Project Architecture & Stack
- **Backend**: Java 21, Spring Boot 3.2.5 (MVC, Data JPA, Validation, Actuator)
- **Frontend**: Responsive HTML5, Bootstrap 5.3, FontAwesome 6, Google Fonts
- **Database**: Embedded H2 Database (In-Memory + Web Console at `/h2-console`)
- **Build Automation**: Apache Maven 3.9.6
- **Continuous Integration**: Jenkins Declarative `Jenkinsfile`
- **End-to-End Testing**: Selenium WebDriver (Headless Chrome/Edge) + JUnit 5
- **Containerization**: Multi-stage `Dockerfile`, `docker-compose.yml`
- **Configuration Management**: Ansible Playbook (`ansible/playbook.yml`) with Idempotency & Rollback

---

## 📁 Repository Structure
```
Devops_project/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── feature_request.md
│   │   └── bug_report.md
│   └── pull_request_template.md
├── ansible/
│   ├── inventory.ini
│   ├── playbook.yml
│   └── rollback.yml
├── docs/
│   ├── 01_PROBLEM_DEFINITION_AND_SCOPE.md
│   ├── 02_AGILE_PLANNING_AND_DEVOPS_WORKFLOW.md
│   ├── 03_SRS_ARCHITECTURE_DATA_MODEL.md
│   ├── 04_GIT_BRANCHING_AND_COLLABORATION_GUIDE.md
│   ├── 05_CONFIGURATION_MANAGEMENT_SPEC.md
│   └── 06_JENKINS_SETUP_AND_CI_CD_GUIDE.md
├── src/
│   ├── main/
│   │   ├── java/com/devops/mrp/
│   │   │   ├── controller/
│   │   │   │   ├── RechargeWebController.java
│   │   │   │   └── RechargeApiController.java
│   │   │   ├── model/
│   │   │   │   ├── RechargePlan.java
│   │   │   │   └── RechargeTransaction.java
│   │   │   ├── repository/
│   │   │   │   ├── RechargePlanRepository.java
│   │   │   │   └── RechargeTransactionRepository.java
│   │   │   ├── service/
│   │   │   │   └── RechargeService.java
│   │   │   └── MobileRechargePortalApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── receipt.html
│   │       │   ├── admin_transactions.html
│   │       │   └── admin_dashboard.html
│   │       └── application.properties
│   └── test/
│       └── java/com/devops/mrp/
│           ├── RechargeServiceTest.java
│           └── RechargeSeleniumTest.java
├── .dockerignore
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── Jenkinsfile
├── pom.xml
└── README.md
```

---

## ⚡ Quick Start (Running Locally)

### 1. Prerequisites Check
Your system currently has:
- Java JDK 21 / 23 installed
- Maven 3.9.6 installed in `c:\tools\apache-maven-3.9.6`

### 2. Build and Run the Application
In your PowerShell / Terminal:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12.1'
$env:Path = "C:\tools\apache-maven-3.9.6\bin;$env:Path"

# Compile and start
mvn spring-boot:run
```

### 3. Access Portal Endpoints
Once started, visit in your browser:
- 🌐 **Home & Catalog**: [http://localhost:8080](http://localhost:8080)
- 📋 **Admin Ledger**: [http://localhost:8080/admin/transactions](http://localhost:8080/admin/transactions)
- 📊 **Executive Dashboard**: [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)
- 🩺 **Health Check**: [http://localhost:8080/api/health](http://localhost:8080/api/health)
- 🗄️ **H2 Web Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:rechargedb`, User: `sa`, Password: empty)

---

## 🧪 Automated Testing

### Run Unit Tests
```powershell
mvn test -Dtest=RechargeServiceTest
```

### Run Selenium Headless E2E Regression Suite
```powershell
mvn test -Dtest=RechargeSeleniumTest
```
*(Failure screenshots will be captured automatically to `target/screenshots/`)*

---

## 🐳 Docker Containerization
```powershell
# Build image
docker build -t devops/mobile-recharge-portal:v1.0.0 .

# Run container
docker run -d --name mrp -p 8080:8080 devops/mobile-recharge-portal:v1.0.0

# Verify health
curl http://localhost:8080/api/health
```

---

## 🔄 CI/CD & Automation
Refer to the detailed step-by-step guides in `docs/`:
- [`docs/01_PROBLEM_DEFINITION_AND_SCOPE.md`](docs/01_PROBLEM_DEFINITION_AND_SCOPE.md)
- [`docs/02_AGILE_PLANNING_AND_DEVOPS_WORKFLOW.md`](docs/02_AGILE_PLANNING_AND_DEVOPS_WORKFLOW.md)
- [`docs/03_SRS_ARCHITECTURE_DATA_MODEL.md`](docs/03_SRS_ARCHITECTURE_DATA_MODEL.md)
- [`docs/04_GIT_BRANCHING_AND_COLLABORATION_GUIDE.md`](docs/04_GIT_BRANCHING_AND_COLLABORATION_GUIDE.md)
- [`docs/05_CONFIGURATION_MANAGEMENT_SPEC.md`](docs/05_CONFIGURATION_MANAGEMENT_SPEC.md)
- [`docs/06_JENKINS_SETUP_AND_CI_CD_GUIDE.md`](docs/06_JENKINS_SETUP_AND_CI_CD_GUIDE.md)
