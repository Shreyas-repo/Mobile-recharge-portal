# 📱 Mobile Recharge Portal (MRP)
## Phase 1: Problem Definition, Stakeholders, Objectives & 15-Task MVP Scope

---

### 1. Problem Statement
In the modern digital telecom ecosystem, prepaid mobile subscribers frequently encounter friction when recharging cellular numbers across diverse telecom operators (e.g., Airtel, Jio, Vi, BSNL). Traditional vendor portals suffer from:
- Cluttered interfaces causing selection of wrong operator/circle plans.
- Lack of immediate transaction visibility and ambiguous failure/pending statuses.
- Inflexible operational workflows where customer support cannot audit, search, or transition transaction states quickly.
- High operational overhead and brittle deployment pipelines with zero automated testing or containerization.

The **Mobile Recharge Portal (MRP)** solves this by providing a unified, responsive, role-based platform that allows customers to effortlessly search plans, initiate recharges, and track live status, while administrators can audit, verify, and manage lifecycle states through a real-time dashboard.

---

### 2. Target User Personas
| Persona | Role | Key Needs & Expectations |
| :--- | :--- | :--- |
| **Prepaid Subscriber (Customer)** | End-User | Instant plan discovery by operator/circle, 1-click recharge submission, immediate transaction confirmation receipt with reference ID. |
| **Support Operator / Finance Admin** | Administrator | Search transaction logs by mobile number/operator/status, filter records, update transaction status (Pending $\rightarrow$ Success / Failed), inspect revenue metrics. |
| **DevOps / Release Engineer** | Operations | Fully automated build, automated regression testing, containerized packaging, automated deployment, and server configuration scripts. |

---

### 3. Existing Pain Points
1. **Operator Confusion**: Users enter their 10-digit number and often pick invalid denomination plans.
2. **Ambiguous Statuses**: Recharge requests hang in "In Progress" without status workflow transparency.
3. **Manual Verification Bottlenecks**: Support staff lack a unified search and filterable status-change workbench.
4. **Deploy Fragility**: Manual server configurations cause environment drift, undetected bugs in production, and downtime during updates.

---

### 4. Stakeholder Matrix
- **Sponsors & Product Owner**: Defines business goals, approves 15-task scope, reviews acceptance criteria.
- **Telecom API Aggregators / Payment Gateways**: External interfaces simulated for plan fetching and transaction processing.
- **Development Team**: Builds frontend, REST backend, H2 persistence, and unit/integration tests.
- **QA Automation Engineers**: Designs Selenium WebDriver end-to-end regression test suites.
- **DevOps Engineers**: Maintains Git branching model, Jenkins CI/CD pipeline, Dockerization, and Ansible configuration scripts.

---

### 5. Constraints
- **Zero-Friction Local Execution**: Must run cleanly on developer machines without complex enterprise cloud licensing.
- **Standardized Stack**: Java 17/23 + Spring Boot Web + Maven + Embedded H2 Database + Embedded Tomcat.
- **Browser Automation**: Selenium tests must support headless browser execution for CI environments.
- **Container Portability**: Dockerfile must be lightweight, multi-stage, and isolated.

---

### 6. Measurable Success Criteria
| Metric | Baseline | Target Success Criteria |
| :--- | :--- | :--- |
| **Recharge Initiation Time** | > 90 seconds | < 15 seconds from entry to confirmation |
| **Search & Filter Latency** | Manual SQL lookups | Instant UI search (< 100ms response) |
| **CI Build & Unit Test Duration** | Manual script | < 2 minutes automated via Jenkins |
| **Automated Test Coverage** | 0% | 100% of critical user journeys covered via Selenium |
| **Container Startup Time** | N/A | < 10 seconds to healthy status in Docker |

---

### 7. Frozen 15-Task MVP Scope

The minimum viable product is strictly scoped into 15 atomic tasks across the sprint:

| Task ID | Component | Task Description | Priority |
| :---: | :--- | :--- | :---: |
| **TASK-01** | Architecture | Project bootstrap with Maven, Spring Boot, Web, and Thymeleaf dependencies. | High |
| **TASK-02** | Data Model | Entity design for `RechargePlan` and `RechargeTransaction` with H2 repository. | High |
| **TASK-03** | UI Foundation | Responsive, modern dark-mode UI layout with navigation, cards, and notification alerts. | High |
| **TASK-04** | Feature 1 | Operator & Circle catalog viewer with plan filtering (Data, Unlimited, Talktime). | High |
| **TASK-05** | Feature 1 | Recharge Initiation Form with validation (10-digit mobile, amount, plan ID). | High |
| **TASK-06** | Feature 2 | Mock Payment & Instant Transaction Generation with unique Transaction Reference ID. | High |
| **TASK-07** | Feature 2 | Transaction Confirmation View & Digital Receipt presentation. | Medium |
| **TASK-08** | Feature 2 | Real-time Transaction Ledger (View All records with paginated/sorted table). | High |
| **TASK-09** | Feature 2 | Live Search & Filter Bar (search by Mobile Number, Operator, or Status). | High |
| **TASK-10** | Feature 2 | Role-based Status Workflow engine (transition from `PENDING` $\rightarrow$ `SUCCESS` / `FAILED`). | High |
| **TASK-11** | Feature 2 | Analytics Dashboard (Total Recharge Volume, Success Rate, Operator Distribution). | Medium |
| **TASK-12** | Git & CI | Git branching policy, GitHub issues, `.gitignore`, and Jenkinsfile definition. | High |
| **TASK-13** | QA Testing | Selenium WebDriver test suite covering 4 core user journeys with failure screenshots. | High |
| **TASK-14** | Packaging | Multi-stage Dockerfile creation and container lifecycle automation scripts. | High |
| **TASK-15** | Provisioning | Ansible automation playbook for target server setup, prerequisites, and service verification. | Medium |
