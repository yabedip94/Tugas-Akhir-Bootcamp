package id.co.juaracoding.hadir.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver initDriver() {
        return initDriver(Config.getBrowser());
    }

    public static WebDriver initDriver(String browserName) {
        if (driverThreadLocal.get() == null) {
            WebDriver driver;
            String browser = (browserName != null) ? browserName.toLowerCase() : "chrome";

            switch (browser) {
                case "chrome":
                default:
                    ChromeOptions options = new ChromeOptions();
                    if (Config.isHeadless()) {
                        options.addArguments("--headless=new");
                    }
                    options.addArguments("--remote-allow-origins=*");
                    options.addArguments("--start-maximized");
                    driver = new ChromeDriver(options);
                    break;
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // Prefer explicit waits
            driver.manage().window().maximize();
            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            return initDriver();
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}

