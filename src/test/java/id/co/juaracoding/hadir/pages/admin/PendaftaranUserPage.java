package id.co.juaracoding.hadir.pages.admin;

import id.co.juaracoding.hadir.pages.BasePage;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object Model untuk halaman Pendaftaran User
 * pada aplikasi HADIR.
 *
 * Flow:
 * Admin Login
 * -> Dashboard
 * -> Management
 * -> Pendaftaran User
 * -> Isi Form
 * -> Submit
 * -> Duplicate = SKIP
 * -> Success = Redirect + Verify User
 */
public class PendaftaranUserPage extends BasePage {

    // Regex pattern untuk halaman User Management List
    private static final String USER_MANAGEMENT_URL_REGEX = ".*/management/user($|/|\\?.*)";

    // =========================================================
    // PAGE
    // =========================================================

    private final By registrasiUserHeading = By.xpath("//h1[normalize-space()='Registrasi User']");

    private final By pendaftaranUserHeader = By.xpath("//header//p[text()='Pendaftaran User']");

    // =========================================================
    // FORM INPUTS
    // =========================================================

    private final By photoInput = By.cssSelector("input[name='logo']");

    private final By nikInput = By.id("nik");

    private final By fullnameInput = By.id("fullname");

    private final By emailInput = By.id("email");

    private final By passwordInput = By.id("password");

    // =========================================================
    // MANDATORY DROPDOWNS
    // =========================================================

    private final By divisiInput = By.id("divisi");

    private final By unitInput = By.id("unit");

    private final By posisiKerjaInput = By.id("posisi-kerja");

    private final By jabatanInput = By.id("jabatan");

    private final By tipeKontrakInput = By.id("tipe-kontrak");

    private final By jadwalKerjaInput = By.id("jadwal-kerja");

    private final By selfieSelectBox = By.id("required_selfie");

    private final By selfieHiddenInput = By.name("required_selfie");

    // =========================================================
    // OPTIONS
    // =========================================================

    private final By firstAutocompleteOption = By.xpath(
            "//li[@role='option' or contains(@class, 'MuiAutocomplete-option')]");

    private final By jabatanKaryawanOption = By.xpath(
            "//li[@role='option' and normalize-space()='Karyawan']");

    private final By tipeKontrakPKWTOption = By.xpath(
            "//li[@role='option' and normalize-space()='PKWT']");

    private final By selfieOption = By.xpath(
            "//li[@role='option' and contains(normalize-space(), 'Selfie')]");

    // =========================================================
    // SUBMIT
    // =========================================================

    private final By submitButton = By.id("submit");

    // =========================================================
    // ALERT & SNACKBAR
    // =========================================================

    private final By registrationAlert = By.xpath(
            "//div[@role='alert']"
                    + " | //div[contains(@class, 'MuiAlert-message')]"
                    + " | //div[contains(@class, 'MuiSnackbarContent-message')]");

    // =========================================================
    // USER MANAGEMENT
    // =========================================================

    private final By userSearchInput = By.id("search");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PendaftaranUserPage(WebDriver driver) {
        super(driver);
    }

    // =========================================================
    // PAGE VERIFICATION
    // =========================================================

    public boolean isPendaftaranUserPageDisplayed() {

        return isDisplayed(registrasiUserHeading);
    }

    public void waitUntilPageDisplayed() {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        registrasiUserHeading));
    }

    public boolean isPendaftaranUserHeaderDisplayed() {

        return isDisplayed(pendaftaranUserHeader);
    }

    // =========================================================
    // PHOTO UPLOAD
    // =========================================================

    public void uploadDummyPhoto() {

        try {

            File tempFile = File.createTempFile("temp_user_photo", ".png");

            tempFile.deleteOnExit();

            byte[] png1x1 = new byte[] {
                    (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D,
                    0x49, 0x48, 0x44, 0x52, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08,
                    0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, (byte) 0xC4, (byte) 0xCD, 0x00, 0x00, 0x00,
                    0x0D, 0x49, 0x44, 0x41, 0x54, 0x78, (byte) 0x9C, 0x63, 0x60, (byte) 0xF8, 0x0F,
                    0x00, 0x01, 0x05, 0x01, 0x02, (byte) 0xD6, (byte) 0xE2, (byte) 0xA9, 0x7C, 0x00,
                    0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44, (byte) 0xAE, 0x42, 0x60, (byte) 0x82
            };

            Files.write(tempFile.toPath(), png1x1);

            driver.findElement(photoInput).sendKeys(tempFile.getAbsolutePath());

        } catch (Exception ignored) {

        }
    }

    // =========================================================
    // FORM ACTIONS
    // =========================================================

    public void inputNik(String nik) {

        type(nikInput, nik);
    }

    public void inputFullname(String fullname) {

        type(fullnameInput, fullname);
    }

    public void inputEmail(String email) {

        type(emailInput, email);
    }

    public void inputPassword(String password) {

        type(passwordInput, password);
    }

    // =========================================================
    // DROPDOWNS SELECTION
    // =========================================================

    private void openAutocomplete(By inputLocator) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        inputLocator));

        driver.findElement(inputLocator).sendKeys(Keys.ARROW_DOWN);
    }

    public void selectDivisiFirstOption() {

        openAutocomplete(divisiInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstAutocompleteOption));

        click(firstAutocompleteOption);
    }

    public void selectUnitFirstOption() {

        openAutocomplete(unitInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstAutocompleteOption));

        click(firstAutocompleteOption);
    }

    public void selectPosisiKerjaFirstOption() {

        openAutocomplete(posisiKerjaInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstAutocompleteOption));

        click(firstAutocompleteOption);
    }

    public void selectJabatanKaryawan() {

        openAutocomplete(jabatanInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        jabatanKaryawanOption));

        click(jabatanKaryawanOption);
    }

    public void selectTipeKontrakPKWT() {

        openAutocomplete(tipeKontrakInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        tipeKontrakPKWTOption));

        click(tipeKontrakPKWTOption);
    }

    public void selectJadwalKerjaFirstOption() {

        openAutocomplete(jadwalKerjaInput);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstAutocompleteOption));

        click(firstAutocompleteOption);
    }

    public void selectSelfieOption() {

        click(selfieSelectBox);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        selfieOption));

        click(selfieOption);
    }

    public void fillAllMandatoryFields(String nik, String fullname, String email, String password) {

        uploadDummyPhoto();

        inputNik(nik);

        inputFullname(fullname);

        inputEmail(email);

        inputPassword(password);

        selectDivisiFirstOption();

        selectUnitFirstOption();

        selectPosisiKerjaFirstOption();

        selectJabatanKaryawan();

        selectTipeKontrakPKWT();

        selectJadwalKerjaFirstOption();

        selectSelfieOption();
    }

    private boolean isElementValueEmpty(By locator) {

        try {

            String val = driver.findElement(locator).getDomProperty("value");

            return val == null || val.trim().isEmpty();

        } catch (Exception e) {

            return true;
        }
    }

    private void ensureMandatoryDropdownsFilled() {

        uploadDummyPhoto();

        if (isElementValueEmpty(divisiInput)) {

            selectDivisiFirstOption();
        }

        if (isElementValueEmpty(unitInput)) {

            selectUnitFirstOption();
        }

        if (isElementValueEmpty(posisiKerjaInput)) {

            selectPosisiKerjaFirstOption();
        }

        if (isElementValueEmpty(jadwalKerjaInput)) {

            selectJadwalKerjaFirstOption();
        }

        if (isElementValueEmpty(selfieHiddenInput)) {

            selectSelfieOption();
        }
    }

    // =========================================================
    // SUBMIT
    // =========================================================

    public void clickSubmit() {

        ensureMandatoryDropdownsFilled();

        click(submitButton);
    }

    // =========================================================
    // RESULT DETECTION
    // =========================================================

    private String capturedAlertMessage = "";

    public void waitForRegistrationResult() {

        capturedAlertMessage = "";

        WebDriverWait resultWait = new WebDriverWait(
                driver,
                Duration.ofSeconds(8));

        try {

            resultWait.until(
                    ExpectedConditions.or(
                            ExpectedConditions.urlMatches(
                                    USER_MANAGEMENT_URL_REGEX),
                            ExpectedConditions.visibilityOfElementLocated(
                                    registrationAlert)));

        } catch (Exception e) {

            // timeout
        }

        try {

            WebElement alertEl = driver.findElement(registrationAlert);

            if (alertEl.isDisplayed()) {

                capturedAlertMessage = alertEl.getText();
            }

        } catch (Exception e) {

            // element not found or already hidden
        }
    }

    public String getRegistrationResultMessage() {

        if (capturedAlertMessage != null && !capturedAlertMessage.trim().isEmpty()) {

            return capturedAlertMessage;
        }

        try {

            WebElement alertEl = driver.findElement(registrationAlert);

            return alertEl.getText();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // DUPLICATE DETECTION
    // =========================================================

    public boolean isDuplicateNikRegistration(String message) {

        if (message == null || message.trim().isEmpty()) {

            return false;
        }

        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("tidak ditemukan")) {

            return false;
        }

        return lowerMessage.contains("nik")
                && (lowerMessage.contains("terdaftar")
                || lowerMessage.contains("sudah")
                || lowerMessage.contains("digunakan")
                || lowerMessage.contains("exist"));
    }

    public boolean isDuplicateEmailRegistration(String message) {

        if (message == null || message.trim().isEmpty()) {

            return false;
        }

        String lowerMessage = message.toLowerCase();

        return lowerMessage.contains("email")
                && (lowerMessage.contains("terdaftar")
                || lowerMessage.contains("sudah")
                || lowerMessage.contains("digunakan"));
    }

    public boolean isSuccessRegistration(String message) {

        if (message == null || message.trim().isEmpty()) {

            return false;
        }

        String lowerMessage = message.toLowerCase();

        return lowerMessage.contains("berhasil")
                || lowerMessage.contains("success");
    }

    public boolean isDuplicateRegistration(String message) {

        return isDuplicateNikRegistration(message)
                || isDuplicateEmailRegistration(message);
    }

    // =========================================================
    // USER MANAGEMENT
    // =========================================================

    public void waitUntilUserManagementDisplayed() {

        wait.until(
                ExpectedConditions.urlMatches(
                        USER_MANAGEMENT_URL_REGEX));
    }

    public boolean isUserManagementDisplayed() {

        String currentUrl = driver.getCurrentUrl();

        return currentUrl != null
                && currentUrl.matches(USER_MANAGEMENT_URL_REGEX);
    }

    public void searchUserByNik(String nik) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userSearchInput));

        type(userSearchInput, nik);

        try {

            By searchButton = By.xpath(
                    "//button[@type='submit' and contains(., 'Search')]");

            if (isDisplayed(searchButton)) {

                click(searchButton);

            } else {

                driver.findElement(userSearchInput)
                        .sendKeys(Keys.ENTER);
            }

            Thread.sleep(1500);

        } catch (Exception e) {

            // silent catch
        }
    }

    public By getUserRowByNik(String nik) {

        return By.xpath(
                "//tr[.//h6[contains(text(), '"
                        + nik
                        + "')] or contains(., '"
                        + nik
                        + "')]");
    }

    public By getUserRowByEmail(String email) {

        return By.xpath(
                "//tr[.//h6[contains(text(), '"
                        + email
                        + "')] or contains(., '"
                        + email
                        + "')]");
    }

    public boolean isUserDisplayedByNik(String nik) {

        return isDisplayed(
                getUserRowByNik(nik));
    }

    public boolean isUserDisplayedByEmail(String email) {

        return isDisplayed(
                getUserRowByEmail(email));
    }

    public By getUserRowByFullname(String fullname) {

        return By.xpath(
                "//tr[.//h5[contains(text(), '"
                        + fullname
                        + "')] or contains(., '"
                        + fullname
                        + "')]");
    }

    public boolean isUserDisplayedByFullname(String fullname) {

        return isDisplayed(
                getUserRowByFullname(fullname));
    }
}