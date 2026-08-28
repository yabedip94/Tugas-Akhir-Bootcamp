# Hadir Selenium Automation

QA Automation project for the HADIR application, covering functional, negative, and cross-role end-to-end testing using Selenium WebDriver, Java, TestNG, and Cucumber.

> **Status:** Under Active Development (Initial Project Bootstrap Phase)

---

## Technology Stack

- **Language:** Java (JDK 17)
- **Build Tool:** Apache Maven
- **Web Automation:** Selenium WebDriver
- **Test Framework:** TestNG
- **BDD Framework:** Cucumber Java & Cucumber TestNG
- **Design Pattern:** Page Object Model (POM)

---

## Project Structure

```text
hadir-selenium-automation/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       ├── java/
│       │   └── id/
│       │       └── co/
│       │           └── juaracoding/
│       │               └── hadir/
│       │                   ├── pages/
│       │                   │   ├── employee/
│       │                   │   │   ├── LoginPage.java
│       │                   │   │   ├── RegisterPage.java
│       │                   │   │   ├── DashboardPage.java
│       │                   │   │   ├── AttendancePage.java
│       │                   │   │   ├── CorrectionPage.java
│       │                   │   │   ├── PermissionPage.java
│       │                   │   │   ├── OvertimePage.java
│       │                   │   │   ├── LeavePage.java
│       │                   │   │   ├── SickPage.java
│       │                   │   │   ├── ReportPage.java
│       │                   │   │   └── StaffPage.java
│       │                   │   │
│       │                   │   └── admin/
│       │                   │       ├── AdminLoginPage.java
│       │                   │       ├── AdminDashboardPage.java
│       │                   │       └── EmployeeApprovalPage.java
│       │                   │
│       │                   ├── stepdefinitions/
│       │                   │   ├── employee/
│       │                   │   │   ├── LoginSteps.java
│       │                   │   │   ├── RegisterSteps.java
│       │                   │   │   ├── DashboardSteps.java
│       │                   │   │   ├── AttendanceSteps.java
│       │                   │   │   ├── CorrectionSteps.java
│       │                   │   │   ├── PermissionSteps.java
│       │                   │   │   ├── OvertimeSteps.java
│       │                   │   │   ├── LeaveSteps.java
│       │                   │   │   ├── SickSteps.java
│       │                   │   │   ├── ReportSteps.java
│       │                   │   │   ├── StaffSteps.java
│       │                   │   │   └── LogoutSteps.java
│       │                   │   │
│       │                   │   └── admin/
│       │                   │       ├── AdminLoginSteps.java
│       │                   │       └── EmployeeApprovalSteps.java
│       │                   │
│       │                   ├── runners/
│       │                   │   └── TestRunner.java
│       │                   │
│       │                   └── utils/
│       │                       ├── DriverFactory.java
│       │                       ├── ScreenshotUtils.java
│       │                       └── TestDataUtils.java
│       │
│       └── resources/
│           ├── features/
│           │   ├── employee/
│           │   │   ├── login.feature
│           │   │   ├── register.feature
│           │   │   ├── dashboard.feature
│           │   │   ├── attendance.feature
│           │   │   ├── correction.feature
│           │   │   ├── permission.feature
│           │   │   ├── overtime.feature
│           │   │   ├── leave.feature
│           │   │   ├── sick.feature
│           │   │   ├── report.feature
│           │   │   ├── staff.feature
│           │   │   └── logout.feature
│           │   │
│           │   ├── admin/
│           │   │   ├── admin_login.feature
│           │   │   └── employee_approval.feature
│           │   │
│           │   └── e2e/
│           │       └── employee_full_journey.feature
│           │
│           └── config/
│               └── config.properties
│
├── pom.xml
├── testng.xml
├── README.md
└── .gitignore
```

---

## Planned Test Scope

- **Employee Module:**
  - Login & Logout
  - Registration & Profile setup
  - Dashboard overview
  - Attendance check-in/check-out
  - Attendance correction requests
  - Permission requests
  - Overtime requests
  - Leave & Sick reporting
  - Report viewing
  - Staff directory & management
- **Admin Module:**
  - Admin Login & Dashboard
  - Employee approval workflow
- **End-to-End (E2E) Journey:**
  - Complete employee lifecycle and cross-role verification

---

## Architecture Flow

```text
Gherkin Feature
        ↓
Step Definition
        ↓
Page Object Model
        ↓
Selenium WebDriver
        ↓
HADIR Application
```

---

## Prerequisites

- Java Development Kit (JDK 17+)
- Apache Maven (3.8+)
- Google Chrome browser (or compatible modern web browser)

---

## Basic Setup & Execution

1. **Clone repository:**
   ```bash
   git clone https://github.com/yabedip94/Tugas-Akhir-Bootcamp-.git
   cd Tugas-Akhir-Bootcamp-
   ```

2. **Compile project:**
   ```bash
   mvn clean compile
   ```

3. **Run tests via Maven:**
   ```bash
   mvn test
   ```
