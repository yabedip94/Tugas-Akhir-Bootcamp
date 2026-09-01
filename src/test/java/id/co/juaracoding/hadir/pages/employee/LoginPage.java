package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import id.co.juaracoding.hadir.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk halaman Login Employee aplikasi HADIR.
 */
public class LoginPage extends BasePage {

    // Lokator elemen halaman Login Employee berdasarkan hasil inspeksi DOM
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector("div[role='alert']");
    private final By monthButton = By.id("month");
    private final By welcomeText = By.xpath("//*[contains(normalize-space(), 'Hai, Hadir SQA Testing 1')]");
    private final By attendanceStatusButton = By.xpath("//button[contains(., 'Absen Masuk') or contains(., 'Keluar')]");

    /**
     * Konstruktor LoginPage yang menginisialisasi WebDriver melalui superclass
     * BasePage.
     *
     * @param driver instansi WebDriver yang digunakan
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman Login Employee berdasarkan URL utama dari file konfigurasi.
     */
    public void openLoginPage() {
        navigateTo(Config.getBaseUrl());
    }

    /**
     * Mengisi alamat email ke dalam kolom email.
     *
     * @param email alamat email pengguna
     */
    public void inputEmail(String email) {
        type(emailField, email);
    }

    /**
     * Mengisi kata sandi ke dalam kolom password.
     *
     * @param password kata sandi pengguna
     */
    public void inputPassword(String password) {
        type(passwordField, password);
    }

    /**
     * Menekan tombol Masuk / Login.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Melakukan alur login lengkap dengan menginputkan email, password, dan menekan
     * tombol login. Menunggu navigasi ke /apps/absent atau kemunculan pesan error.
     *
     * @param email    alamat email pengguna
     * @param password kata sandi pengguna
     */
    public void login(String email, String password) {
        inputEmail(email);
        inputPassword(password);
        clickLoginButton();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.and(
                        ExpectedConditions.urlContains("/apps/absent"),
                        ExpectedConditions.visibilityOfElementLocated(attendanceStatusButton)
                ),
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        ));
    }

    /**
     * Mengambil teks pesan kesalahan yang muncul saat proses login gagal.
     *
     * @return teks pesan kesalahan dari elemen alert
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Memeriksa apakah halaman utama absensi (dashboard employee) tampil setelah
     * login berhasil.
     *
     * @return true jika elemen tombol bulan (button#month) tampil di layar, false
     *         jika tidak
     */
    public boolean isAttendancePageDisplayed() {
        return isDisplayed(monthButton);
    }

    /**
     * Memeriksa apakah teks ucapan selamat tampil di halaman setelah login
     * berhasil.
     *
     * @return true jika teks ucapan selamat tampil, false jika tidak
     */
    public boolean isWelcomeTextDisplayed() {
        return isDisplayed(welcomeText);
    }
}
