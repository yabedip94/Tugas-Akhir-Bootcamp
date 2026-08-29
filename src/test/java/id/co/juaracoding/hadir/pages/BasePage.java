package id.co.juaracoding.hadir.pages;

import id.co.juaracoding.hadir.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Kelas abstrak induk untuk semua kelas Page Object Model.
 * Menyediakan objek WebDriver, WebDriverWait, serta metode pembantu yang dapat digunakan kembali.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Konstruktor dasar untuk menginisialisasi WebDriver dan PageFactory dengan batas waktu dari konfigurasi.
     *
     * @param driver instansi WebDriver yang digunakan
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getExplicitWaitTimeout()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Konstruktor alternatif untuk menginisialisasi WebDriver dengan batas waktu khusus.
     *
     * @param driver instansi WebDriver yang digunakan
     * @param timeoutInSeconds durasi batas waktu Explicit Wait dalam detik
     */
    public BasePage(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        PageFactory.initElements(driver, this);
    }

    // --- Metode Explicit Wait Menggunakan Lokator By ---

    /**
     * Menunggu elemen hingga terlihat di layar berdasarkan lokator By.
     */
    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Menunggu elemen hingga siap diklik berdasarkan lokator By.
     */
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementVisible(By locator) {
        return waitForVisible(locator);
    }

    public WebElement waitForElementClickable(By locator) {
        return waitForClickable(locator);
    }

    // --- Metode Explicit Wait Menggunakan WebElement ---

    /**
     * Menunggu elemen hingga terlihat di layar.
     */
    public WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Menunggu elemen hingga siap diklik.
     */
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementVisible(WebElement element) {
        return waitForVisible(element);
    }

    public WebElement waitForElementClickable(WebElement element) {
        return waitForClickable(element);
    }

    // --- Metode Aksi Interaksi Menggunakan Lokator By ---

    /**
     * Menunggu elemen siap diklik kemudian melakukan aksi klik.
     */
    public void click(By locator) {
        waitForClickable(locator).click();
        demoDelay();
    }

    /**
     * Menunggu elemen terlihat, mengosongkan isi teks sebelumnya, lalu mengetikkan teks baru.
     */
    public void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
        demoDelay();
    }

    public void sendKeys(By locator, String text) {
        type(locator, text);
    }

    /**
     * Menampilkan dan mengambil teks dari elemen.
     */
    public String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    /**
     * Memeriksa apakah elemen tampil di layar.
     */
    public boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Metode Aksi Interaksi Menggunakan WebElement ---

    /**
     * Menunggu elemen siap diklik kemudian melakukan aksi klik.
     */
    public void click(WebElement element) {
        waitForClickable(element).click();
        demoDelay();
    }

    /**
     * Menunggu elemen terlihat, mengosongkan isi teks sebelumnya, lalu mengetikkan teks baru.
     */
    public void type(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
        demoDelay();
    }

    public void sendKeys(WebElement element, String text) {
        type(element, text);
    }

    /**
     * Menampilkan dan mengambil teks dari elemen.
     */
    public String getText(WebElement element) {
        return waitForVisible(element).getText();
    }

    /**
     * Memeriksa apakah elemen tampil di layar.
     */
    public boolean isDisplayed(WebElement element) {
        try {
            return waitForVisible(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Navigasi dan Fungsi Pembantu ---

    /**
     * Membuka alamat URL yang ditentukan.
     */
    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Mengambil URL halaman yang sedang dibuka.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Mengambil judul halaman saat ini.
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Mencari daftar elemen berdasarkan lokator By.
     */
    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Memberikan jeda waktu (delay) antar-aksi jika mode demo diaktifkan.
     */
    protected void demoDelay() {
        if (!Config.isDemoMode()) {
            return;
        }
        int delay = Config.getDemoDelay();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
