# ⚙️ Phase 13 & 14: Configuration Management & Reliability Specification
## Mobile Recharge Portal (MRP)

---

### 1. Server Prerequisites Matrix
| Category | Requirement | Specification |
| :--- | :--- | :--- |
| **Operating System** | Linux (Ubuntu 22.04 LTS / Debian 12 / RHEL 9) | Host runtime |
| **Packages** | `openjdk-21-jre-headless`, `curl`, `ufw` | Core application dependencies |
| **System Users** | `mrpuser` (system user without login shell) | Security least-privilege principle |
| **System Groups** | `mrpuser` | Group ownership |
| **Directories** | `/opt/mobile-recharge-portal`, `/var/log/mobile-recharge-portal` | Permissions: `0755`, Owner: `mrpuser:mrpuser` |
| **Artifacts** | `mobile-recharge-portal-1.0.0-SNAPSHOT.jar` | Permissions: `0644` |
| **Network Ports** | `8080/tcp` | Inbound web traffic allowed |
| **Systemd Service**| `/etc/systemd/system/mrp.service` | Auto-restart on failure, memory limits (`-Xms256m -Xmx512m`) |

---

### 2. Ansible Playbook Architecture
The configuration code is structured inside the `ansible/` directory:
- `ansible/inventory.ini`: Target server nodes and global variables.
- `ansible/playbook.yml`: Provisioning, installation, deployment, and live verification.
- `ansible/rollback.yml`: Automated disaster recovery to restore `app-previous-stable.jar`.

---

### 3. Idempotency Demonstration
**What is Idempotency?**
Executing the automation script multiple times on the same target node must produce identical outcomes without duplicate processes, altered permissions, or server instability.

#### Verification Steps:
1. **First Execution**:
   ```bash
   ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
   ```
   *Expected Result*: Tasks show `changed=10, failed=0` (packages installed, directories created, service started).

2. **Second Execution (Idempotency Proof)**:
   ```bash
   ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
   ```
   *Expected Result*: Tasks show `ok=11, changed=0, failed=0`. No new files duplicated, service remains running undisturbed.

---

### 4. Health Check & Rollback Validation
1. **Live Health Check**:
   The playbook executes a REST probe:
   ```bash
   curl -i http://localhost:8080/api/health
   ```
   *Response*:
   ```json
   HTTP/1.1 200 OK
   Content-Type: application/json
   {
     "status": "UP",
     "application": "Mobile Recharge Portal",
     "version": "1.0.0-MVP",
     "timestamp": "2026-10-21T12:30:00"
   }
   ```

2. **Rollback & Disaster Recovery Scenario**:
   - If a newly deployed build fails health check or introduces a regression:
   ```bash
   ansible-playbook -i ansible/inventory.ini ansible/rollback.yml
   ```
   - Restores `/opt/mobile-recharge-portal/app-previous-stable.jar`.
   - Restarts the `mrp.service`.
   - Re-runs health check to confirm service restoration within < 10 seconds.
