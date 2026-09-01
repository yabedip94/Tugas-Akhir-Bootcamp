package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Page object for the "Sick" module (Sakit).
 * Converted from SakitMobilePage reference implementation.
 */
public class SickPage extends BasePage {

    // --- Navigation ---
    private final By sickMenuIcon = By.xpath(
            "//a[(contains(@class,'user__menu__item') or contains(@class,'user_menu_item')) and .//p[normalize-space()='Sakit']] | //a[contains(@href,'sick') or contains(@href,'sakit')]");

    // --- Form & Page Locators ---
    private final By sickPageTitle = By.xpath("//p[normalize-space(.)='Halaman Sakit']");
    private final By submitSickButton = By.xpath("//button[normalize-space(.)='Ajukan Sakit']");
    private final By totalSickRequestText = By.xpath("//p[contains(., 'Total :')]");
    private final By sickFormDrawerTitle = By.xpath("//div[contains(@class,'MuiBox-root')]//p[text()='Ajukan Request Sakit']");
    private final By selectSickDateTrigger = By.xpath("//div[contains(@class,'MuiFormControl-root')][.//label[normalize-space(text())='Pilih Tanggal']]//div[contains(@class,'MuiBox-root')]");
    private final By startDateInput = By.cssSelector("input[placeholder='Early']");
    private final By endDateInput = By.cssSelector("input[placeholder='Continuous']");
    private final By saveDateButton = By.xpath("//button[normalize-space(.)='Simpan']");
    private final By uploadFileInput = By.cssSelector("input[type='file']");
    private final By formSubmitButton = By.cssSelector("button[type='submit']");
    private final By reuploadButton = By.xpath("//button[normalize-space(text())='Upload ulang']");
    private final By firstEntryStartDateText = By.xpath("(//p[contains(., 'Dari tanggal')])[1]");

    public SickPage(WebDriver driver) {
        super(driver);
    }

    public void openSickMenu() {
        waitForVisible(
                By.xpath("//*[contains(text(),'Hai,')] | //a[contains(@href,'sick') or contains(@href,'sakit') or contains(@class,'menu')]"));
        for (int i = 0; i < 3; i++) {
            try {
                WebElement menu = waitForClickable(sickMenuIcon);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menu);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
                waitForVisible(sickPageTitle);
                return;
            } catch (Exception e) {
                if (i == 2) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public String readSickTitle() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(sickPageTitle, "Halaman Sakit"));
        return getText(sickPageTitle);
    }

    public int readTotalSickRequests() {
        waitForVisible(totalSickRequestText);
        String text = getText(totalSickRequestText).replace("Total :", "").trim();
        int result = Integer.parseInt(text);
        System.out.println("TOTAL MENTAH: [" + result + "]");
        return result;
    }

    public void openSickForm() {
        waitForClickable(submitSickButton).click();
    }

    public void waitForSickFormDrawer() {
        waitForVisible(sickFormDrawerTitle);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String readSickFormDrawerTitle() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(sickFormDrawerTitle, "Ajukan Request Sakit"));
        return getText(sickFormDrawerTitle);
    }

    public String selectSickDate(int daysFromNow) {
        LocalDate date = LocalDate.now().plusDays(daysFromNow);
        String startDate = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));
        String checkerStartDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        String endDate = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));
        System.out.println("TANGGAL: [" + startDate + "] s/d [" + endDate + "]");

        click(selectSickDateTrigger);

        WebElement col1 = waitForVisible(startDateInput);
        col1.click();
        col1.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        col1.sendKeys(startDate);

        WebElement col2 = waitForVisible(endDateInput);
        col2.click();
        col2.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        col2.sendKeys(endDate);

        System.out.println("KOLOM AWAL: [" + col1.getDomProperty("value") + "]");
        System.out.println("KOLOM AKHIR: [" + col2.getDomProperty("value") + "]");

        waitForClickable(saveDateButton).click();
        System.out.println("RINGKASAN TANGGAL: [" + getText(selectSickDateTrigger) + "]");

        return checkerStartDate;
    }

    public void uploadSickDocument() {
        WebElement inputFile = driver.findElement(uploadFileInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('hidden');", inputFile);
        File file = new File(System.getProperty("user.dir") + "/src/test/resources/surat-sakit.jpg");
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir") + "/src/test/resources/fixtures/sample_selfie.png");
        }
        inputFile.sendKeys(file.getAbsolutePath());
        System.out.println("FILE: [" + inputFile.getDomProperty("value") + "]");
    }

    public void clickSubmitSick() {
        waitForClickable(formSubmitButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(sickFormDrawerTitle));
    }

    public void waitForReuploadButton() {
        waitForVisible(reuploadButton);
    }

    public void waitForTotalChanged(int oldTotal) {
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElementLocated(
                        totalSickRequestText, "Total : " + oldTotal)));
    }

    public String readFirstEntryStartDate() {
        waitForVisible(firstEntryStartDateText);
        String text = getText(firstEntryStartDateText);
        System.out.println("TANGGAL ENTRI: [" + text + "]");
        return text;
    }
}
