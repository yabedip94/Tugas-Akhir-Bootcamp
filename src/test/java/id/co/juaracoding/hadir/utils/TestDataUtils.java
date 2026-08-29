package id.co.juaracoding.hadir.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Utilitas untuk membaca data pengujian dan kredensial akun dari berkas .env-hadir.
 */
public class TestDataUtils {

    private static final Properties properties = new Properties();

    static {
        loadEnvProperties();
    }

    /**
     * Membaca berkas .env-hadir dari direktori utama proyek.
     */
    private static void loadEnvProperties() {
        File envFile = new File(".env-hadir");
        if (envFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(envFile)) {
                properties.load(inputStream);
            } catch (Exception e) {
                System.err.println("Gagal membaca berkas .env-hadir: " + e.getMessage());
            }
        }
    }

    /**
     * Mengambil nilai konfigurasi atau kredensial berdasarkan kunci.
     * Mengutamakan environment variable sistem jika tersedia.
     *
     * @param key kunci properti
     * @return nilai properti atau string kosong jika tidak ditemukan
     */
    public static String get(String key) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return properties.getProperty(key, "").trim();
    }

    /**
     * Mengambil nama pengguna / email untuk Employee 1.
     */
    public static String getEmployee1Username() {
        return get("USERNAME_EMPLOYEE_1");
    }

    /**
     * Mengambil kata sandi untuk Employee 1.
     */
    public static String getEmployee1Password() {
        return get("PASSWORD_EMPLOYEE_1");
    }

    /**
     * Mengambil nama pengguna / email untuk Employee 2.
     */
    public static String getEmployee2Username() {
        return get("USERNAME_EMPLOYEE_2");
    }

    /**
     * Mengambil kata sandi untuk Employee 2.
     */
    public static String getEmployee2Password() {
        return get("PASSWORD_EMPLOYEE_2");
    }

    /**
     * Mengambil nama pengguna / email untuk Employee 3.
     */
    public static String getEmployee3Username() {
        return get("USERNAME_EMPLOYEE_3");
    }

    /**
     * Mengambil kata sandi untuk Employee 3.
     */
    public static String getEmployee3Password() {
        return get("PASSWORD_EMPLOYEE_3");
    }
}
