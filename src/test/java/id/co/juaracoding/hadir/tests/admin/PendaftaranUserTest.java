package id.co.juaracoding.hadir.tests.admin;

import id.co.juaracoding.hadir.pages.admin.AdminDashboardPage;
import id.co.juaracoding.hadir.pages.admin.AdminLoginPage;
import id.co.juaracoding.hadir.pages.admin.PendaftaranUserPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * E2E Test Pendaftaran User Admin dengan Business Logic Validation.
 *
 * Candidate Master NIKs: D7240002, D7240054, D8240001, D8240100
 *
 * Rules:
 * 1. "NIK sudah terdaftar" -> Duplicate NIK -> SKIP
 * 2. "Email sudah terdaftar" -> Duplicate Email -> SKIP
 * 3. "NIK Anda tidak ditemukan" -> Invalid NIK / Not in master -> FAIL
 * 4. Error lain bukan duplicate -> FAIL
 * 5. Successful registration -> redirect exact /management/user -> search -> verify -> PASS
 */
public class PendaftaranUserTest extends BaseTest {

    @Test
    public void testPendaftaranUser() {

        // =====================================================
        // TEST DATA VALID MASTER KANDIDAT NIK & UNIK EMAIL
        // =====================================================

        String testNik = "D7240002";

        String testFullname = "Test Karyawan D7240002";

        String testEmail = "sqa.user." + System.currentTimeMillis() + "@gmail.com";

        String testPassword = "Password123";

        // =====================================================
        // 1. LOGIN ADMIN
        // =====================================================

        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);

        adminLoginPage.openLoginPage();

        String username = TestDataUtils.getAdminUsername();

        String password = TestDataUtils.getAdminPassword();

        adminLoginPage.login(
                username,
                password);

        // =====================================================
        // 2. VERIFY DASHBOARD
        // =====================================================

        AdminDashboardPage adminDashboardPage = new AdminDashboardPage(driver);

        Assert.assertTrue(
                adminDashboardPage.isAdminDashboardDisplayed(),
                "Admin gagal masuk ke Dashboard Admin.");

        // =====================================================
        // 3. OPEN MANAGEMENT
        // =====================================================

        adminDashboardPage.clickManagement();

        // =====================================================
        // 4. OPEN PENDAFTARAN USER
        // =====================================================

        adminDashboardPage.clickPendaftaranUser();

        // =====================================================
        // 5. VERIFY PAGE
        // =====================================================

        PendaftaranUserPage pendaftaranUserPage = new PendaftaranUserPage(driver);

        pendaftaranUserPage.waitUntilPageDisplayed();

        Assert.assertTrue(
                pendaftaranUserPage.isPendaftaranUserPageDisplayed(),
                "Halaman Pendaftaran User gagal dibuka.");

        // =====================================================
        // 6. INPUT FORM LENGKAP
        // =====================================================

        pendaftaranUserPage.fillAllMandatoryFields(
                testNik,
                testFullname,
                testEmail,
                testPassword);

        // =====================================================
        // 7. SUBMIT FORM
        // =====================================================

        pendaftaranUserPage.clickSubmit();

        // =====================================================
        // 8. TUNGGU HASIL REGISTRASI
        // =====================================================

        pendaftaranUserPage.waitForRegistrationResult();

        // =====================================================
        // 9. DAPATKAN ALERT MESSAGE (JIKA ADA)
        // =====================================================

        String alertMessage = pendaftaranUserPage.getRegistrationResultMessage();

        // =====================================================
        // 10. HANDLING "NIK Anda tidak ditemukan" -> FAIL
        // =====================================================

        if (alertMessage != null && alertMessage.toLowerCase().contains("tidak ditemukan")) {

            Assert.fail("[FAIL] NIK tidak terdaftar pada master aplikasi: " + alertMessage);
        }

        // =====================================================
        // 11. HANDLING DUPLICATE NIK = PRINT + SKIP
        // =====================================================

        if (pendaftaranUserPage.isDuplicateNikRegistration(alertMessage)) {

            String skipMsg = "[SKIP] Duplicate NIK: " + alertMessage;

            System.out.println(skipMsg);

            throw new SkipException(skipMsg);
        }

        // =====================================================
        // 12. HANDLING DUPLICATE EMAIL = PRINT + SKIP
        // =====================================================

        if (pendaftaranUserPage.isDuplicateEmailRegistration(alertMessage)) {

            String skipMsg = "[SKIP] Duplicate Email: " + alertMessage;

            System.out.println(skipMsg);

            throw new SkipException(skipMsg);
        }

        // =====================================================
        // 13. ERROR LAIN BUKAN DUPLICATE & BUKAN SUCCESS = FAIL
        // =====================================================

        if (alertMessage != null && !alertMessage.trim().isEmpty()
                && !pendaftaranUserPage.isSuccessRegistration(alertMessage)
                && !pendaftaranUserPage.isUserManagementDisplayed()) {

            Assert.fail("[FAIL] Registrasi gagal dengan alert: " + alertMessage);
        }

        // =====================================================
        // 14. SUCCESS = WAIT & VERIFY REDIRECT EXACT TO /management/user
        // =====================================================

        try {

            pendaftaranUserPage.waitUntilUserManagementDisplayed();

        } catch (Exception e) {

            // timeout wait if redirect did not complete
        }

        Assert.assertTrue(
                pendaftaranUserPage.isUserManagementDisplayed(),
                "Registrasi tidak menghasilkan redirect ke User Management.");

        // =====================================================
        // 15. SEARCH USER BY NIK
        // =====================================================

        pendaftaranUserPage.searchUserByNik(testNik);

        // =====================================================
        // 16. VERIFY ROW USER (NIK, NAMA, EMAIL)
        // =====================================================

        Assert.assertTrue(
                pendaftaranUserPage.isUserDisplayedByNik(testNik),
                "User berhasil redirect ke User Management, tetapi NIK "
                        + testNik + " tidak ditemukan di tabel.");

        Assert.assertTrue(
                pendaftaranUserPage.isUserDisplayedByFullname(testFullname),
                "User berhasil redirect ke User Management, tetapi Nama Karyawan "
                        + testFullname + " tidak ditemukan di tabel.");

        Assert.assertTrue(
                pendaftaranUserPage.isUserDisplayedByEmail(testEmail),
                "User berhasil redirect ke User Management, tetapi Email "
                        + testEmail + " tidak ditemukan di tabel.");
    }
}