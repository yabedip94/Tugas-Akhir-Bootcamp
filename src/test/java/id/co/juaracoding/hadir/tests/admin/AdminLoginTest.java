package id.co.juaracoding.hadir.tests.admin;

import id.co.juaracoding.hadir.pages.admin.AdminLoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test automation untuk fitur Login Admin aplikasi HADIR.
 *
 * Skenario:
 * 1. Positive Login dengan credential Admin valid.
 * 2. Negative Login dengan email dan password kosong.
 * 3. Negative Login dengan password kosong.
 */
public class AdminLoginTest extends BaseTest {

    /**
     * Positive Test:
     * Admin login menggunakan credential yang valid.
     *
     * Expected Result:
     * Admin berhasil masuk ke Dashboard Admin.
     */
    @Test(priority = 1)
    public void testAdminLoginPositive() {

        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);

        // Membuka halaman Login Admin
        adminLoginPage.openLoginPage();

        // Mengambil credential Admin dari .env-hadir
        String username = TestDataUtils.getAdminUsername();
        String password = TestDataUtils.getAdminPassword();

        // Melakukan login Admin
        adminLoginPage.login(username, password);

        // Verifikasi berhasil masuk ke Dashboard Admin
        Assert.assertTrue(
                adminLoginPage.isAdminDashboardDisplayed(),
                "Admin gagal masuk ke Dashboard Admin.");
    }

    /**
     * Negative Test:
     * Admin mencoba login tanpa mengisi email dan password.
     *
     * Expected Result:
     * Admin tidak berhasil login dan tetap berada
     * pada halaman Login Admin.
     */
    @Test(priority = 3)
    public void testAdminLoginNegativeEmptyCredential() {

        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);

        // Membuka halaman Login Admin
        adminLoginPage.openLoginPage();

        // Klik tombol Masuk tanpa mengisi credential
        adminLoginPage.clickLoginButton();

        // Verifikasi Admin tidak masuk ke Dashboard
        Assert.assertFalse(
                driver.getCurrentUrl().contains("/dashboards/pending"),
                "Admin berhasil masuk ke Dashboard menggunakan credential kosong.");

        // Verifikasi tetap berada di halaman Login Admin
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/authentication/login"),
                "Admin tidak berada di halaman Login setelah empty credential.");
    }

    /**
     * Negative Test:
     * Admin mencoba login dengan email valid tetapi password kosong.
     *
     * Expected Result:
     * Admin tidak berhasil login dan tidak diarahkan
     * ke Dashboard Admin.
     */
    @Test(priority = 2)
    public void testAdminLoginNegativeEmptyPassword() {

        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);

        // Membuka halaman Login Admin
        adminLoginPage.openLoginPage();

        // Mengambil email Admin dari .env-hadir
        String username = TestDataUtils.getAdminUsername();

        // Mengisi email dengan credential valid
        adminLoginPage.inputEmail(username);

        // Password sengaja dikosongkan
        adminLoginPage.inputPassword("");

        // Klik tombol Masuk
        adminLoginPage.clickLoginButton();

        // Verifikasi Admin tidak masuk ke Dashboard
        Assert.assertFalse(
                driver.getCurrentUrl().contains("/dashboards/pending"),
                "Admin berhasil masuk ke Dashboard dengan password kosong.");

        // Verifikasi tetap berada di halaman Login Admin
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/authentication/login"),
                "Admin tidak berada di halaman Login setelah password kosong.");
    }
}