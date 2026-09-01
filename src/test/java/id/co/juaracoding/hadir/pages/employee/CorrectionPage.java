package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object Model untuk halaman Koreksi Absen Employee (/apps/absent/correction).
 * Menangani navigasi menu, pembukaan modal, pengisian datetime picker, dan verifikasi validasi.
 */
public class CorrectionPage extends BasePage {

    // Lokator menu "Koreksi Absen" pada dashboard employee (/apps/absent)
    private final By menuKoreksiButton = By.xpath("//a[.//p[text()='Koreksi Absen']]");

    // Lokator judul halaman "Halaman Koreksi"
    private final By pageTitle = By.xpath("//p[normalize-space()='Halaman Koreksi']");

    // Lokator judul section "List Koreksi"
    private final By listKoreksiHeader = By.xpath("//p[normalize-space()='List Koreksi']");

    // Lokator tombol "Ajukan Koreksi" di halaman utama koreksi
    private final By ajukanKoreksiButton = By.xpath("//button[normalize-space()='Ajukan Koreksi']");

    // Lokator judul modal "Ajukan Koreksi Absen"
    private final By modalTitle = By.xpath("//p[normalize-space()='Ajukan Koreksi Absen']");

    // Lokator input field Jam Masuk dan Jam Keluar
    private final By jamMasukInput = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[1]");
    private final By jamKeluarInput = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[2]");

    // Lokator tombol ikon kalender/jam untuk membuka datetime picker MUI berdasarkan label field
    private final By jamMasukPickerButton = By.xpath("//label[text()='Jam masuk']/following-sibling::div//button[contains(@aria-label, 'Choose date')]");
    private final By jamKeluarPickerButton = By.xpath("//label[text()='Jam keluar']/following-sibling::div//button[contains(@aria-label, 'Choose date')]");

    // Lokator tombol tanggal pada kalender picker MUI yang sedang aktif
    private final By pickerDayButton = By.xpath("//div[contains(@class, 'MuiPickersPopper-root')]//button[contains(@class, 'MuiPickersDay-today') or (contains(@class, 'MuiPickersDay-root') and not(@disabled))]");

    // Lokator combobox dropdown Tipe Absen (WFH / WFO)
    private final By tipeAbsenCombobox = By.id("is_wfh");

    // Lokator tombol submit "Ajukan" di dalam modal formulir
    private final By submitAjukanButton = By.xpath("//button[@type='submit' and normalize-space()='Ajukan']");

    // Lokator tombol "Reset" di dalam modal formulir
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");

    // Lokator pesan validasi error "Salah satu harus diisi!"
    private final By validationErrorMessage = By.xpath("//p[normalize-space()='Salah satu harus diisi!']");

    // Lokator daftar card riwayat koreksi absen
    private final By correctionCards = By.xpath("//div[contains(@class, 'MuiCard-root')]");

    public CorrectionPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman Koreksi Absen melalui menu "Koreksi Absen" pada dashboard employee.
     */
    public void clickMenuKoreksiAbsen() {
        WebElement element = waitForClickable(menuKoreksiButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element);

        try {
            waitForClickable(menuKoreksiButton).click();
        } catch (StaleElementReferenceException e) {
            waitForClickable(menuKoreksiButton).click();
        }
        demoDelay();
    }

    /**
     * Menunggu hingga halaman Koreksi Absen selesai dimuat (/apps/absent/correction)
     * dan judul "Halaman Koreksi" terlihat.
     */
    public void waitForCorrectionPageReady() {
        wait.until(ExpectedConditions.urlContains("/apps/absent/correction"));
        waitForVisible(pageTitle);
    }

    /**
     * Memeriksa apakah judul halaman "Halaman Koreksi" tampil di layar.
     *
     * @return true jika judul halaman tampil, false jika tidak
     */
    public boolean isPageTitleVisible() {
        return isDisplayed(pageTitle);
    }

    /**
     * Mengambil teks judul halaman.
     *
     * @return teks judul halaman
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Memeriksa apakah header "List Koreksi" tampil di layar.
     *
     * @return true jika header List Koreksi tampil, false jika tidak
     */
    public boolean isListKoreksiVisible() {
        return isDisplayed(listKoreksiHeader);
    }

    /**
     * Menekan tombol "Ajukan Koreksi" untuk membuka modal formulir pengajuan koreksi absen.
     */
    public void clickAjukanKoreksi() {
        click(ajukanKoreksiButton);
    }

    /**
     * Menunggu hingga modal "Ajukan Koreksi Absen" terlihat di layar.
     */
    public void waitForModalVisible() {
        waitForVisible(modalTitle);
    }

    /**
     * Memeriksa apakah modal formulir koreksi absen sedang tampil di layar.
     *
     * @return true jika modal tampil, false jika tidak
     */
    public boolean isModalVisible() {
        return isDisplayed(modalTitle);
    }

    /**
     * Menunggu hingga modal formulir koreksi absen tertutup sepenuhnya.
     */
    public void waitForModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
    }

    /**
     * Memilih tanggal dan jam untuk kolom Jam Masuk menggunakan datetime picker MUI.
     */
    public void selectJamMasukFromPicker() {
        click(jamMasukPickerButton);
        click(pickerDayButton);
    }

    /**
     * Memilih tanggal dan jam untuk kolom Jam Keluar menggunakan datetime picker MUI.
     */
    public void selectJamKeluarFromPicker() {
        click(jamKeluarPickerButton);
        click(pickerDayButton);
    }

    /**
     * Memilih Tipe Absen (WFH / WFO) dari dropdown combobox MUI.
     *
     * @param tipe nilai data-value dari opsi yang dipilih (misal: "wfh" atau "wfo")
     */
    public void selectTipeAbsen(String tipe) {
        click(tipeAbsenCombobox);

        By option = By.xpath(
                "//li[@role='option' and @data-value='" + tipe + "']"
        );

        click(option);
    }

    /**
     * Mengambil teks Tipe Absen yang sedang terpilih pada combobox.
     *
     * @return teks Tipe Absen terpilih
     */
    public String getTipeAbsenValue() {
        return waitForVisible(tipeAbsenCombobox).getText().trim();
    }

    /**
     * Mengambil nilai teks aktual pada kolom input Jam Masuk tanpa memicu peringatan deprecation.
     *
     * @return nilai teks Jam Masuk
     */
    public String getJamMasukValue() {
        String val = waitForVisible(jamMasukInput).getDomProperty("value");
        return val != null ? val : "";
    }

    /**
     * Mengambil nilai teks aktual pada kolom input Jam Keluar tanpa memicu peringatan deprecation.
     *
     * @return nilai teks Jam Keluar
     */
    public String getJamKeluarValue() {
        String val = waitForVisible(jamKeluarInput).getDomProperty("value");
        return val != null ? val : "";
    }

    /**
     * Menekan tombol submit "Ajukan" di dalam modal formulir koreksi absen.
     */
    public void clickSubmitAjukan() {
        click(submitAjukanButton);
    }

    /**
     * Menekan tombol "Reset" di dalam modal formulir koreksi absen.
     */
    public void clickReset() {
        click(resetButton);
    }

    /**
     * Memeriksa apakah pesan error validasi tampil di dalam formulir.
     *
     * @return true jika pesan error validasi tampil, false jika tidak
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(validationErrorMessage);
    }

    /**
     * Mengambil teks pesan error validasi yang tampil.
     *
     * @return teks pesan error validasi
     */
    public String getErrorMessageText() {
        return getText(validationErrorMessage);
    }

    /**
     * Mengambil daftar seluruh WebElement card riwayat koreksi yang tampil.
     *
     * @return daftar WebElement card koreksi
     */
    public List<WebElement> getCorrectionCards() {
        return findElements(correctionCards);
    }
}
