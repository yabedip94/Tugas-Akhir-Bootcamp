package id.co.juaracoding.hadir.utils;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_PATH = "config/config.properties";

    static {
        loadProperties(DEFAULT_CONFIG_PATH);
    }

    private static void loadProperties(String path) {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("Warning: Configuration file not found at " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading configuration file from " + path + ": " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static String getBaseUrl() {
        return get("baseUrl", "");
    }

    public static int getTimeout() {
        String timeoutStr = get("timeout", "10");
        try {
            return Integer.parseInt(timeoutStr.trim());
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static boolean isHeadless() {
        String headlessStr = get("headless", "false");
        return Boolean.parseBoolean(headlessStr.trim());
    }
}
