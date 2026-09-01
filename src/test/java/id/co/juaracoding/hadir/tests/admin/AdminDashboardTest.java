package id.co.juaracoding.hadir.tests.admin;

import id.co.juaracoding.hadir.pages.admin.AdminDashboardPage;
import id.co.juaracoding.hadir.pages.admin.AdminLoginPage;
import id.co.juaracoding.hadir.pages.admin.PendaftaranUserPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test automation untuk fitur Dashboard Admin aplikasi HADIR.
 *
 * Positive Test:
 * Admin login menggunakan credential valid,
 * masuk ke Dashboard Admin,
 * membuka menu Management,
 * memilih Pendaftaran User,
 * dan berhasil membuka halaman Pendaftaran User.
 */
public class AdminDashboardTest extends BaseTest {

    /**
     * Positive E2E Test:
     *
     * LOGIN ADMIN
     * -> ADMIN DASHBOARD
     * -> MANAGEMENT
     * -> PENDAFTARAN USER
     * -> VERIFY HALAMAN PENDAFTARAN USER
     *
     * Test ini dibuat independent sehingga dapat dijalankan
     * tanpa bergantung pada AdminLoginTest.
     */
    @Test(priority = 1)
    public void testAdminDashboardBukaPendaftaranUser() {

        // ==============================
        // 1. LOGIN ADMIN
        // ==============================

        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);

        adminLoginPage.openLoginPage();

        String username = TestDataUtils.getAdminUsername();

        String password = TestDataUtils.getAdminPassword();

        adminLoginPage.login(username, password);

        // ==============================
        // 2. VERIFIKASI ADMIN DASHBOARD
        // ==============================

        AdminDashboardPage adminDashboardPage = new AdminDashboardPage(driver);

        Assert.assertTrue(
                adminDashboardPage.isAdminDashboardDisplayed(),
                "Admin gagal masuk ke Dashboard Admin.");

        // ==============================
        // 3. BUKA MENU MANAGEMENT
        // ==============================

        adminDashboardPage.clickManagement();

        // ==============================
        // 4. BUKA PENDAFTARAN USER
        // ==============================

        adminDashboardPage.clickPendaftaranUser();

        // ==============================
        // 5. VERIFIKASI HALAMAN
        // PENDAFTARAN USER
        // ==============================

        PendaftaranUserPage pendaftaranUserPage = new PendaftaranUserPage(driver);

        Assert.assertTrue(
                pendaftaranUserPage.isPendaftaranUserPageDisplayed(),
                "Halaman Pendaftaran User gagal dibuka.");
    }
}