package id.co.juaracoding.hadir.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v132.emulation.Emulation;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Pabrik pengelolaan instansi WebDriver menggunakan ThreadLocal agar aman untuk
 * eksekusi paralel.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Membuka dan menyiapkan WebDriver menggunakan jenis peramban dari file
     * konfigurasi dengan izin kamera dan lokasi diizinkan secara default.
     *
     * @return instansi WebDriver yang telah disiapkan
     */
    public static WebDriver initDriver() {
        return initDriver(Config.getBrowser(), true, true);
    }

    /**
     * Membuka dan menyiapkan WebDriver berdasarkan nama peramban yang diberikan
     * dengan izin kamera dan lokasi diizinkan secara default.
     *
     * @param browserName nama peramban yang ingin dijalankan (misal: "chrome")
     * @return instansi WebDriver yang telah disiapkan
     */
    public static WebDriver initDriver(String browserName) {
        return initDriver(browserName, true, true);
    }

    /**
     * Membuka dan menyiapkan WebDriver dengan kontrol izin khusus untuk kamera
     * dan geolokasi (digunakan untuk isolasi pengujian negatif).
     *
     * @param browserName      nama peramban yang ingin dijalankan
     * @param allowCamera      true jika kamera diizinkan, false jika diblokir
     * @param allowGeolocation true jika lokasi diizinkan, false jika diblokir
     * @return instansi WebDriver yang telah disiapkan
     */
    public static WebDriver initDriver(String browserName, boolean allowCamera, boolean allowGeolocation) {
        if (driverThreadLocal.get() == null) {
            WebDriver driver;
            String browser = (browserName != null && !browserName.trim().isEmpty()) ? browserName.toLowerCase().trim()
                    : "chrome";

            switch (browser) {
                case "chrome":
                default:
                    ChromeOptions options = new ChromeOptions();
                    if (Config.isHeadless()) {
                        options.addArguments("--headless=new");
                    }
                    options.addArguments("--remote-allow-origins=*");
                    options.addArguments("--start-maximized");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--disable-notifications");
                    options.addArguments("--disable-save-password-bubble");
                    options.addArguments("--password-store=basic");

                    Map<String, Object> prefs = new HashMap<>();

                    // --- Menonaktifkan Password Manager & Leak Detection Pop-up ---
                    prefs.put("credentials_enable_service", false);
                    prefs.put("profile.password_manager_enabled", false);
                    prefs.put("profile.password_manager_leak_detection", false);
                    prefs.put("autofill.profile_enabled", false);
                    prefs.put("autofill.credit_card_enabled", false);

                    // --- Konfigurasi Kamera ---
                    if (allowCamera) {
                        // Kamera palsu agar browser tidak error tanpa hardware kamera
                        options.addArguments("--use-fake-device-for-media-stream");
                        options.addArguments("--use-fake-ui-for-media-stream");
                        prefs.put("profile.default_content_setting_values.media_stream_camera", 1); // 1 = Allow
                    } else {
                        // Memblokir izin akses kamera untuk pengujian negatif kamera
                        prefs.put("profile.default_content_setting_values.media_stream_camera", 2); // 2 = Block
                    }

                    // --- Konfigurasi Geolokasi ---
                    if (allowGeolocation) {
                        prefs.put("profile.default_content_setting_values.geolocation", 1); // 1 = Allow
                    } else {
                        // Memblokir izin akses lokasi untuk pengujian negatif geolokasi
                        prefs.put("profile.default_content_setting_values.geolocation", 2); // 2 = Block
                    }

                    options.setExperimentalOption("prefs", prefs);
                    driver = new ChromeDriver(options);
                    break;
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            driver.manage().window().maximize();

            // Atur lokasi CDP hanya jika geolokasi diizinkan
            if (allowGeolocation) {
                configureFakeGeolocation(driver);
            } else {
                clearFakeGeolocation(driver);
            }

            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    /**
     * Mengonfigurasi lokasi palsu (geolocation) menggunakan Chrome DevTools
     * Protocol (CDP).
     *
     * @param driver instansi WebDriver yang sedang aktif
     */
    private static void configureFakeGeolocation(WebDriver driver) {
        if (driver instanceof HasDevTools) {
            try {
                DevTools devTools = ((HasDevTools) driver).getDevTools();
                devTools.createSession();
                devTools.send(Emulation.setGeolocationOverride(
                        Optional.of(-6.200000), // latitude
                        Optional.of(106.816666), // longitude
                        Optional.of(100.0) // accuracy (meter)
                ));
            } catch (Exception e) {
                System.err.println("Peringatan: Gagal mengatur geolocation palsu via CDP: " + e.getMessage());
            }
        }
    }

    /**
     * Menghapus penimpaan lokasi CDP untuk skenario pengujian negatif lokasi.
     *
     * @param driver instansi WebDriver yang sedang aktif
     */
    private static void clearFakeGeolocation(WebDriver driver) {
        if (driver instanceof HasDevTools) {
            try {
                DevTools devTools = ((HasDevTools) driver).getDevTools();
                devTools.createSession();
                devTools.send(Emulation.clearGeolocationOverride());
            } catch (Exception e) {
                // Ignore jika belum dibuat session
            }
        }
    }

    /**
     * Mengambil instansi WebDriver aktif untuk thread saat ini.
     * Jika belum diinisialisasi, metode ini akan memanggil initDriver().
     *
     * @return instansi WebDriver aktif
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            return initDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Menutup sesi WebDriver dan menghapus instansi dari ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Gagal menutup instansi WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
