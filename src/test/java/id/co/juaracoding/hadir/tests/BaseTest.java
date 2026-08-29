package id.co.juaracoding.hadir.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.WebDriver;
import id.co.juaracoding.hadir.utils.DriverFactory;

/**
 * Kelas dasar (BaseTest) untuk seluruh Selenium TestNG.
 * Bertanggung jawab mengelola pembuatan (setup) dan pembersihan (cleanup) WebDriver
 * sebelum dan sesudah setiap metode pengujian dijalankan.
 */
public class BaseTest {

    protected WebDriver driver;

    /**
     * Metode setup yang dijalankan sebelum setiap metode pengujian (@Test).
     * Inisialisasi WebDriver melalui DriverFactory dan menyimpannya di variabel driver.
     */
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();
    }

    /**
     * Metode tearDown yang dijalankan setelah setiap metode pengujian (@Test).
     * Menutup WebDriver melalui DriverFactory dan mereset variabel driver menjadi null.
     */
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        driver = null;
    }
}
