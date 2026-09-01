package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object untuk halaman Registrasi Employee (HADIR).
 *
 * Mengelola interaksi dengan formulir registrasi employee:
 * NIK, Nama Lengkap, Email, Password, Upload Selfie, serta penanganan Alert.
 */
public class RegisterPage extends BasePage {

    // Lokator Formulir Registrasi (ID stabil 100% unik)
    private final By nikInput = By.id("nik");
    private final By fullnameInput = By.id("fullname");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By selfieInput = By.id("selfie");
    private final By submitButton = By.id("submit");

    // Lokator Notification Alert (MUI Alert)
    private final By alertContainer = By.cssSelector(".MuiAlert-root, [class*='MuiAlert-root']");
    private final By alertMessage = By.cssSelector(".MuiAlert-message, [class*='MuiAlert-message']");
    private final By successAlert = By.cssSelector(".MuiAlert-filledSuccess");
    private final By selfieErrorText = By.cssSelector("p.MuiTypography-body1.css-1g9yqtk");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman registrasi employee.
     */
    public void openRegisterPage() {
        navigateTo("https://magang.dikahadir.com/absen/register");
    }

    /**
     * Mengisi kolom NIK.
     *
     * @param nik NIK kandidat
     */
    public void inputNik(String nik) {
        type(nikInput, nik);
    }

    /**
     * Mengisi kolom Nama Lengkap.
     *
     * @param fullname Nama lengkap
     */
    public void inputFullname(String fullname) {
        type(fullnameInput, fullname);
    }

    /**
     * Mengisi kolom Email.
     *
     * @param email Alamat email
     */
    public void inputEmail(String email) {
        type(emailInput, email);
    }

    /**
     * Mengisi kolom Password.
     *
     * @param password Kata sandi
     */
    public void inputPassword(String password) {
        type(passwordInput, password);
    }

    /**
     * Mengunggah berkas foto selfie.
     *
     * @param absoluteFilePath Jalur absolut file gambar selfie
     */
    public void uploadSelfie(String absoluteFilePath) {
        waitForVisible(selfieInput).sendKeys(absoluteFilePath);
        demoDelay();
    }

    /**
     * Menekan tombol "Daftar".
     */
    public void clickSubmit() {
        click(submitButton);
    }

    /**
     * Menunggu hingga respon registrasi (MUI Alert) tampil di layar.
     *
     * @return Teks pesan alert yang muncul
     */
    public String waitForRegistrationResult() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
            return alert.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Mengambil teks pesan dari MUI Alert.
     *
     * @return Teks alert
     */
    public String getAlertText() {
        try {
            return waitForVisible(alertMessage).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Memeriksa apakah MUI Alert tampil di layar tanpa pemicu explicit wait timeout.
     *
     * @return true jika alert tampil, false jika tidak
     */
    public boolean isAlertDisplayed() {
        try {
            return !driver.findElements(alertContainer).isEmpty() && driver.findElement(alertContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah alert bertipe Sukses tampil di layar tanpa pemicu explicit wait timeout.
     *
     * @return true jika alert sukses tampil, false jika tidak
     */
    public boolean isSuccessAlertDisplayed() {
        try {
            return !driver.findElements(successAlert).isEmpty() && driver.findElement(successAlert).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa pesan kesalahan khusus selfie jika file invalid/kosong.
     *
     * @return Teks error selfie
     */
    public String getSelfieErrorMessage() {
        try {
            return waitForVisible(selfieErrorText).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Memeriksa keabsahan HTML5 client-side validation pada kolom NIK.
     *
     * @return true jika valid, false jika terhalang HTML5 validation
     */
    public boolean isNikInputValid() {
        return checkHtml5Validity(nikInput);
    }

    /**
     * Memeriksa keabsahan HTML5 client-side validation pada kolom Email.
     *
     * @return true jika valid, false jika terhalang HTML5 validation
     */
    public boolean isEmailInputValid() {
        return checkHtml5Validity(emailInput);
    }

    /**
     * Helper privat untuk mengeksekusi checkValidity() via JavaScript.
     */
    private boolean checkHtml5Validity(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", element);
        } catch (Exception e) {
            return true;
        }
    }
}
