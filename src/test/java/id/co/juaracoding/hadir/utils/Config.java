package id.co.juaracoding.hadir.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Kelas utilitas untuk membaca dan mengelola konfigurasi framework
 * dari berkas config.properties.
 */
public class Config {

    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_PATH = "config/config.properties";

    static {
        loadProperties();
    }

    /**
     * Membaca berkas konfigurasi dari classpath.
     */
    private static void loadProperties() {
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream(DEFAULT_CONFIG_PATH)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println(
                        "Peringatan: Berkas konfigurasi tidak ditemukan: "
                                + DEFAULT_CONFIG_PATH);
            }

        } catch (Exception e) {
            System.err.println(
                    "Gagal membaca berkas konfigurasi: "
                            + e.getMessage());
        }
    }

    /**
     * Mengambil nilai konfigurasi berdasarkan kunci.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Mengambil nilai konfigurasi berdasarkan kunci
     * dengan nilai bawaan.
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Mengambil URL utama aplikasi.
     */
    public static String getBaseUrl() {
        return get("base.url", "https://hadir.juaracoding.com");
    }

    /**
     * Mengambil jenis peramban yang digunakan.
     */
    public static String getBrowser() {
        return get("browser", "chrome");
    }

    /**
     * Memeriksa apakah mode tanpa tampilan diaktifkan.
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                get("headless", "false").trim());
    }

    /**
     * Mengambil batas waktu Explicit Wait dalam detik.
     */
    public static int getExplicitWaitTimeout() {
        String timeout = get("timeout.explicit", "10");

        try {
            return Integer.parseInt(timeout.trim());
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    /**
     * Mengambil batas waktu tunggu default.
     */
    public static int getTimeout() {
        return getExplicitWaitTimeout();
    }

    /**
     * Memeriksa apakah mode demo (Demo Mode) diaktifkan.
     *
     * @return true jika mode demo aktif, false jika tidak
     */
    public static boolean isDemoMode() {
        return Boolean.parseBoolean(get("demo.mode", "false").trim());
    }

    /**
     * Mengambil durasi jeda mode demo (Demo Delay) dalam milidetik.
     *
     * @return durasi jeda dalam milidetik, atau 0 jika nilai tidak valid
     */
    public static int getDemoDelay() {
        String delay = get("demo.delay", "0");
        try {
            return Integer.parseInt(delay.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}