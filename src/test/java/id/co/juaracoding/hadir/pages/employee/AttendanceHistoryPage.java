package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object untuk halaman History Absensi Employee.
 *
 * Menangani navigasi dari dashboard melalui menu Absensi
 * dan pembacaan data pada record history absensi.
 */
public class AttendanceHistoryPage extends BasePage {

    // Menu "Absensi" pada dashboard employee
    private final By menuAbsensiButton = By.xpath("//a[.//p[text()='Absensi']]");

    // Judul halaman History Absensi
    private final By pageTitle = By.xpath("//p[text()='History Absensi']");

    // Container/card record history absensi
    private final By historyCardList = By.xpath("//div[contains(@class, 'image-wrapper')]/parent::div");

    // Penanda bahwa seluruh data history sudah selesai dimuat
    private final By endOfDataMessage = By.xpath("//p[text()='Tidak ada lagi data']");

    public AttendanceHistoryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka History Absensi melalui menu Absensi pada dashboard employee.
     */
    public void clickMenuAbsensi() {
        WebElement element = waitForClickable(menuAbsensiButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element);

        waitForClickable(menuAbsensiButton).click();
        demoDelay();
    }

    /**
     * Menunggu halaman History Absensi siap digunakan.
     *
     * Readiness marker:
     * - URL mengandung /apps/absent/activity
     * - Judul History Absensi terlihat
     */
    public void waitForPageReady() {
        wait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("/apps/absent/activity"),
                ExpectedConditions.visibilityOfElementLocated(pageTitle)));
    }

    /**
     * Mengambil teks judul halaman.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Mengambil seluruh card history absensi yang tersedia.
     *
     * Menunggu sampai:
     * - minimal satu card tampil, atau
     * - indikator akhir data tampil.
     */
    public List<WebElement> getHistoryCards() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(historyCardList),
                ExpectedConditions.visibilityOfElementLocated(endOfDataMessage)));

        return findElements(historyCardList);
    }

    /**
     * Mengambil nama employee dari record.
     */
    public String getEmployeeName(WebElement card) {
        return card.findElement(By.xpath(".//p[1]")).getText();
    }

    /**
     * Mengambil tanggal attendance dari record.
     */
    public String getAttendanceDate(WebElement card) {
        return card.findElement(By.xpath(".//p[2]")).getText();
    }

    /**
     * Mengambil tipe/status attendance dari record.
     */
    public String getAttendanceType(WebElement card) {
        return card.findElement(By.xpath(".//p[3]")).getText();
    }

    /**
     * Mengambil informasi waktu attendance dari record.
     */
    public String getAttendanceTime(WebElement card) {
        return card.findElement(By.xpath(".//p[4]")).getText();
    }

    /**
     * Mengambil notes dari record.
     *
     * Notes dapat berisi "-" atau teks lain,
     * sehingga test cukup memastikan elemennya dapat diakses.
     */
    public String getNotes(WebElement card) {
        return card.findElement(By.xpath(".//p[5]")).getText();
    }
}