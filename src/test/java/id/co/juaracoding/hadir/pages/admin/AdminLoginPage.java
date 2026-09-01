package id.co.juaracoding.hadir.pages.admin;

import id.co.juaracoding.hadir.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk halaman Login Admin aplikasi HADIR.
 */
public class AdminLoginPage extends BasePage {

    // URL khusus Login Admin
    private static final String ADMIN_LOGIN_URL = "https://magang.dikahadir.com/authentication/login";

    // Locator berdasarkan hasil inspeksi DOM aktual menggunakan MCP Playwright
    private final By emailField = By.id("email");

    private final By passwordField = By.id("password");

    private final By loginButton = By.cssSelector("button[type='submit']");

    private final By errorMessage = By.cssSelector("div[role='alert']");

    // Verifikasi Dashboard Admin
    private final By adminProfile = By.xpath("//h5[contains(normalize-space(), 'Admin Hadir')]");

    /**
     * Konstruktor AdminLoginPage.
     *
     * @param driver WebDriver yang digunakan
     */
    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman Login Admin.
     */
    public void openLoginPage() {
        navigateTo(ADMIN_LOGIN_URL);
    }

    /**
     * Mengisi email Admin.
     *
     * @param email email Admin
     */
    public void inputEmail(String email) {
        type(emailField, email);
    }

    /**
     * Mengisi password Admin.
     *
     * @param password password Admin
     */
    public void inputPassword(String password) {
        type(passwordField, password);
    }

    /**
     * Klik tombol Masuk.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Melakukan login Admin.
     *
     * Setelah klik login, menunggu:
     * - Dashboard Admin, atau
     * - pesan error login.
     *
     * @param email    email Admin
     * @param password password Admin
     */
    public void login(String email, String password) {

        inputEmail(email);
        inputPassword(password);
        clickLoginButton();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/dashboards/pending"),
                ExpectedConditions.visibilityOfElementLocated(errorMessage)));
    }

    /**
     * Memeriksa apakah Dashboard Admin berhasil tampil.
     *
     * @return true jika profile Admin Hadir tampil
     */
    public boolean isAdminDashboardDisplayed() {
        return isDisplayed(adminProfile);
    }

    /**
     * Memeriksa apakah halaman Login Admin masih tampil.
     *
     * Digunakan untuk negative test empty credential.
     *
     * @return true jika field email dan password masih tampil
     */
    public boolean isLoginPageDisplayed() {
        return isDisplayed(emailField)
                && isDisplayed(passwordField);
    }

    /**
     * Mengambil pesan error login jika tersedia.
     *
     * @return pesan error
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}