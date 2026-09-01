package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Page object for the "Leave" module (Cuti).
 * Converted from CutiMobilePage reference implementation.
 */
public class LeavePage extends BasePage {

    // --- Navigation ---
    private final By leaveMenuIcon = By.xpath(
            "//a[(contains(@class,'user__menu__item') or contains(@class,'user_menu_item')) and .//p[normalize-space()='Cuti']] | //a[contains(@href,'leave') or contains(@href,'cuti')]");

    // --- Form & Page Locators ---
    private final By leavePageTitle = By.xpath("//p[normalize-space(.)='Halaman Cuti']");
    private final By submitLeaveButton = By.xpath("//button[normalize-space(.)='Ajukan Cuti']");
    private final By totalLeaveRequestText = By.xpath("//p[contains(., 'Total :')]");
    private final By leaveFormDrawerTitle = By.xpath("//div[contains(@class,'MuiDrawer-paper')]//p[text()='Ajukan Cuti']");
    private final By leaveTypeDropdown = By.id("leave_type_id");
    private final By leaveTypeOption = By.xpath("//li[@role='option'][normalize-space(text())='Khitanan/Pembabtisan (2 hari)']");
    private final By selectLeaveDateTrigger = By.xpath("//div[contains(@class,'MuiFormControl-root')][.//label[normalize-space(text())='Pilih Tanggal']]//div[contains(@class,'MuiBox-root')]");
    private final By startDateInput = By.cssSelector("input[placeholder='Early']");
    private final By endDateInput = By.cssSelector("input[placeholder='Continuous']");
    private final By saveDateButton = By.xpath("//button[normalize-space(.)='Simpan']");
    private final By notesTextarea = By.id("notes");
    private final By formSubmitButton = By.cssSelector("button[type='submit']");

    public LeavePage(WebDriver driver) {
        super(driver);
    }

    public void openLeaveMenu() {
        waitForVisible(
                By.xpath("//*[contains(text(),'Hai,')] | //a[contains(@href,'leave') or contains(@href,'cuti') or contains(@class,'menu')]"));
        for (int i = 0; i < 3; i++) {
            try {
                WebElement menu = waitForClickable(leaveMenuIcon);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menu);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
                waitForVisible(leavePageTitle);
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

    public String readLeaveTitle() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(leavePageTitle, "Halaman Cuti"));
        return getText(leavePageTitle);
    }

    public int readTotalLeaveRequests() {
        waitForVisible(totalLeaveRequestText);
        String text = getText(totalLeaveRequestText).replace("Total :", "").trim();
        int result = Integer.parseInt(text);
        System.out.println("TOTAL MENTAH: [" + result + "]");
        return result;
    }

    public void openLeaveForm() {
        waitForClickable(submitLeaveButton).click();
    }

    public void waitForLeaveFormDrawer() {
        waitForVisible(leaveFormDrawerTitle);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void selectLeaveType() {
        waitForClickable(leaveTypeDropdown).click();
        waitForClickable(leaveTypeOption).click();
    }

    public void selectLeaveDates() {
        String startDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));
        String endDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));

        click(selectLeaveDateTrigger);

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
        System.out.println("RINGKASAN TANGGAL: [" + getText(selectLeaveDateTrigger) + "]");
    }

    public void selectPastLeaveDates() {
        String startDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));
        String endDate = LocalDate.now().minusYears(1).plusDays(1).format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));

        click(selectLeaveDateTrigger);

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
        System.out.println("RINGKASAN TANGGAL: [" + getText(selectLeaveDateTrigger) + "]");
    }

    public String fillLeaveNotes(String description) {
        waitForClickable(notesTextarea);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        WebElement textArea = waitForVisible(notesTextarea);
        textArea.click();
        String notes = description + " - " + timestamp;
        textArea.sendKeys(notes);
        System.out.println("CATATAN: [" + textArea.getDomProperty("value") + "]");
        return notes;
    }

    public void clickSubmitLeave() {
        waitForClickable(formSubmitButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(leaveFormDrawerTitle));
    }

    public boolean isLeaveEntryPresent(String notes) {
        By entry = By.xpath("//p[contains(.,'" + notes + "')]");
        try {
            return waitForVisible(entry).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLeaveEntryNotPresent(String notes) {
        By entry = By.xpath("//p[contains(.,'" + notes + "')]");
        System.out.println("JUMLAH ENTRI DITEMUKAN: " + driver.findElements(entry).size());
        return driver.findElements(entry).isEmpty();
    }
}
