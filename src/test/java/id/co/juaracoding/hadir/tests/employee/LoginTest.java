package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian otomatis TestNG untuk fitur Login Employee aplikasi HADIR.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    /**
     * Inisialisasi LoginPage sebelum setiap pengujian dijalankan.
     */
    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    /**
     * Pengujian login berhasil menggunakan kredensial Employee 1.
     */
    @Test(description = "Verifikasi login berhasil dengan kredensial valid Employee 1")
    public void testLoginBerhasilDenganKredensialValid() {

        // Membuka halaman login Employee
        loginPage.openLoginPage();

        // Mengambil kredensial Employee 1 dari berkas .env-hadir
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();

        // Melakukan proses login
        loginPage.login(email, password);

        // Memastikan elemen utama halaman absensi tampil setelah navigasi selesai
        Assert.assertTrue(
                loginPage.isAttendancePageDisplayed(),
                "Halaman utama absensi tidak tampil setelah login berhasil!");

        // Memastikan URL setelah login sesuai dengan halaman absensi Employee
        String expectedUrl = "https://magang.dikahadir.com/apps/absent";
        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(
                actualUrl,
                expectedUrl,
                "URL halaman setelah login tidak sesuai!");

        // Verifikasi nama user setelah login
        Assert.assertTrue(
                loginPage.isWelcomeTextDisplayed(),
                "Nama user tidak tampil setelah login berhasil!");
    }

    /**
     * Pengujian login gagal menggunakan kredensial yang tidak valid.
     */
    @Test(description = "Verifikasi login gagal dengan kredensial tidak valid")
    public void testLoginGagalDenganKredensialTidakValid() {

        // Membuka halaman login Employee
        loginPage.openLoginPage();

        // Melakukan login menggunakan kredensial yang tidak valid
        loginPage.login(
                "invalid-test@example.com",
                "InvalidPassword123!");

        // Memastikan pesan kesalahan login tampil sesuai
        String expectedErrorMessage = "Akun tidak ditemukan";
        String actualErrorMessage = loginPage.getErrorMessage();

        Assert.assertEquals(
                actualErrorMessage,
                expectedErrorMessage,
                "Pesan kesalahan login tidak sesuai!");
    }
}