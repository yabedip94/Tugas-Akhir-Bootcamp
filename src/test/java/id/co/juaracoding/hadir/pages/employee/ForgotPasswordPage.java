package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model untuk fitur Lupa Password (Forgot Password / Reset Password Request).
 * 
 * Mengelola navigasi dari halaman Login, interaksi formulir pengajuan reset password,
 * verifikasi Toast Notifikasi Sukses, serta validasi HTML5 client-side.
 */
public class ForgotPasswordPage extends BasePage {

    // Lokator Halaman Login
    private final By forgotPasswordLink = By.xpath("//button[text()='Lupa password ?']");

    // Lokator Halaman Reset Password Request
    private final By emailInput = By.id("email");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By backToLoginButton = By.xpath("//button[text()='Login']");

    // Lokator Alert / Toast Notifikasi Spesifik
    private final By successToast = By.cssSelector(".MuiAlert-filledSuccess");
    private final By successToastMessage = By.cssSelector(".MuiAlert-filledSuccess .MuiAlert-message");

    // Lokator Halaman Verifikasi OTP / Reset Password (Step 2)
    private final By otpInput = By.id("otp");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman Login aplikasi HADIR.
     */
    public void openLoginPage() {
        navigateTo("https://magang.dikahadir.com/absen/login");
    }

    /**
     * Menekan tombol / link "Lupa password ?" pada halaman Login.
     */
    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    /**
     * Mengisi kolom Email pada formulir reset password request.
     *
     * @param email Alamat email terdaftar
     */
    public void inputEmail(String email) {
        type(emailInput, email);
    }

    /**
     * Menekan tombol "Submit" pengajuan reset password.
     */
    public void clickSubmit() {
        click(submitButton);
    }

    /**
     * Menekan tombol "Login" untuk kembali ke halaman Login.
     */
    public void clickBackToLogin() {
        click(backToLoginButton);
    }

    /**
     * Mengambil teks pesan notifikasi Toast Sukses spesifik.
     *
     * @return Teks notifikasi sukses
     */
    public String getSuccessToastText() {
        try {
            return waitForVisible(successToastMessage).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Memeriksa apakah Success Toast (.MuiAlert-filledSuccess) tampil di layar
     * memanfaatkan explicit wait dari BasePage.
     *
     * @return true jika toast sukses tampil, false jika tidak
     */
    public boolean isSuccessToastDisplayed() {
        try {
            return waitForVisible(successToast).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah kolom input OTP (id="otp") pada halaman reset password tampil di layar
     * memanfaatkan explicit wait dari BasePage.
     *
     * @return true jika kolom OTP tampil, false jika tidak
     */
    public boolean isOtpInputDisplayed() {
        try {
            return waitForVisible(otpInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa keabsahan HTML5 Client-Side Validation pada kolom Email via checkValidity().
     * Tanpa menyamarkan exception teknis agar pengujian transparan.
     *
     * @return true jika valid, false jika terhalang validation (checkValidity() == false)
     */
    public boolean isEmailInputValid() {
        WebElement element = driver.findElement(emailInput);
        return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", element);
    }

    /**
     * Memeriksa apakah terjadi kesalahan tipe format pada kolom Email via validity.typeMismatch.
     * Tanpa menyamarkan exception teknis agar pengujian transparan.
     *
     * @return true jika terjadi typeMismatch (format email salah), false jika tidak
     */
    public boolean isEmailTypeMismatch() {
        WebElement element = driver.findElement(emailInput);
        return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.typeMismatch;", element);
    }
}
