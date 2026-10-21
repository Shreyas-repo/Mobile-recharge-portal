# 🌿 Phase 4: Git Branching, Collaboration & Release Strategy
## Mobile Recharge Portal (MRP)

---

### 1. Branch Naming Policy
All development adheres strictly to the following branching topology:

| Branch Type | Naming Convention | Base Branch | Target Merge | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **Main / Production** | `main` | - | - | Production-ready, tagged releases only. |
| **Development** | `develop` | `main` | `main` | Continuous integration baseline for ongoing sprint features. |
| **Feature Branches** | `feature/<task-id>-<slug>` | `develop` | `develop` | Isolated unit of work (e.g. `feature/task-04-plan-browsing`). |
| **Bugfix Branches** | `bugfix/<issue-id>-<slug>` | `develop` | `develop` | Defect fixes during testing or review. |
| **Release Branches** | `release/v<semver>` | `develop` | `main` & `develop` | Final release hardening and metadata updates. |

---

### 2. Commit Message Standards (Conventional Commits)
Format: `<type>(<scope>): <subject>`

Allowed Types:
- `feat`: A new user-facing feature.
- `fix`: A bug fix.
- `docs`: Documentation updates.
- `test`: Adding or refactoring unit or Selenium tests.
- `ci`: Changes to Jenkinsfile, Dockerfile, or automation scripts.
- `refactor`: Code change that neither fixes a bug nor adds a feature.

*Examples*:
- `feat(catalog): add operator and circle filter for recharge plans`
- `fix(validation): enforce strict 10-digit mobile number format`
- `ci(jenkins): add automated Selenium test execution stage`

---

### 3. Pull Request & Review Workflow
1. Branch from `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/task-05-recharge-form
   ```
2. Make commits following commit standards:
   ```bash
   git add .
   git commit -m "feat(recharge): implement mobile number submission and validation"
   ```
3. Push to remote:
   ```bash
   git push origin feature/task-05-recharge-form
   ```
4. Open Pull Request on GitHub against `develop`.
5. Require at least 1 peer approval and passing automated CI build before merging.

---

### 4. Merge Conflict Simulation & Resolution Guide
To fulfill the DevOps demonstration requirement:
1. **Create Conflict**:
   - On branch `feature/alpha`, modify line 15 of `AppConfig.java` to set timeout = 3000.
   - On branch `feature/beta`, modify the same line to set timeout = 5000.
   - Merge `feature/alpha` into `develop`.
   - Attempting to merge `feature/beta` into `develop` produces `CONFLICT (content): Merge conflict in AppConfig.java`.
2. **Resolve Conflict**:
   ```bash
   git checkout feature/beta
   git merge develop
   # Edit AppConfig.java, resolve <<<<<<< HEAD markers
   git add AppConfig.java
   git commit -m "fix(merge): resolve timeout conflict between alpha and beta branches"
   git push origin feature/beta
   ```

---

### 5. Semantic Tagging & Release Baseline
Once the 15-task MVP scope is verified:
```bash
git checkout main
git merge develop
git tag -a v1.0.0-mvp -m "Release v1.0.0-mvp: Fully functional Mobile Recharge Portal MVP"
git push origin v1.0.0-mvp
```
