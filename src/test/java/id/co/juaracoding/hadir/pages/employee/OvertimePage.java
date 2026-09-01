package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page object for the "Overtime" module (Lembur).
 * Converted from LemburPage reference implementation.
 */
public class OvertimePage extends BasePage {

    // --- Navigation ---
    private final By overtimeMenuIcon = By.xpath(
            "//a[(contains(@class,'user__menu__item') or contains(@class,'user_menu_item')) and .//p[normalize-space()='Lembur']]");

    // --- List + floating action button ---
    private final By listOvertimeTitle = By.xpath("//*[normalize-space()='List Overtime']");
    private final By listOvertimeTotal = By.xpath(
            "//p[normalize-space()='List Overtime']/following::p[contains(text(),'Total :')][1] | //*[contains(text(),'Total :')]");
    private final By submitOvertimeFab = By.xpath("//button[normalize-space()='Ajukan Lembur']");

    // --- Popup form ---
    private final By popupStartTimeInput = By.xpath("//input[@placeholder='dd mm yyyy, hh:mm'][1]");
    private final By popupEndTimeInput = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[2]");
    private final By popupNotesTextarea = By.id("notes");
    private final By popupResetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By popupSubmitButton = By.xpath("//button[@type='submit' and normalize-space()='Ajukan']");

    // --- Feedback after submit ---
    private final By validationErrorMessage = By
            .xpath("//*[contains(@class,'MuiFormHelperText-root') and contains(@class,'Mui-error')]");
    private final By snackbarAlertMessage = By
            .xpath("//*[contains(@class,'MuiSnackbarContent-root') or contains(@class,'MuiAlert-message') or @role='alert']");

    public OvertimePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPopupClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(popupStartTimeInput));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSnackbarAlertShown() {
        try {
            return waitForVisible(snackbarAlertMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSnackbarAlertMessage() {
        try {
            return waitForVisible(snackbarAlertMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void openOvertimeMenu() {
        waitForVisible(
                By.xpath("//*[contains(text(),'Hai,')] | //a[contains(@href,'overtime') or contains(@class,'menu')]"));
        for (int i = 0; i < 3; i++) {
            try {
                WebElement menu = waitForClickable(overtimeMenuIcon);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menu);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menu);
                waitForVisible(listOvertimeTitle);
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

    public void openOvertimeForm() {
        WebElement fab = waitForClickable(submitOvertimeFab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fab);
        waitForVisible(popupStartTimeInput);
    }

    public int getTotalOvertime() {
        String totalText = getText(listOvertimeTotal);
        return Integer.parseInt(totalText.replaceAll("[^0-9]", ""));
    }

    public void submitOvertime(String startTime, String endTime, String notes) {
        openOvertimeForm();
        fillPopupForm(startTime, endTime, notes);
        submitPopup();
    }

    private void setReactInputValue(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var elem = arguments[0];" +
                "var value = arguments[1];" +
                "var prototype = elem.tagName.toLowerCase() === 'textarea' ? window.HTMLTextAreaElement.prototype : window.HTMLInputElement.prototype;"
                +
                "var descriptor = Object.getOwnPropertyDescriptor(prototype, 'value');" +
                "if (descriptor && descriptor.set) { descriptor.set.call(elem, value); } else { elem.value = value; }" +
                "elem.dispatchEvent(new Event('input', { bubbles: true }));" +
                "elem.dispatchEvent(new Event('change', { bubbles: true }));";
        js.executeScript(script, element, value);
    }

    public void fillPopupForm(String startTime, String endTime, String notes) {
        if (startTime != null) {
            WebElement el = waitForVisible(popupStartTimeInput);
            if (!startTime.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript(
                        "var el = arguments[0];" +
                                "var val = arguments[1];" +
                                "var fiber = el[Object.keys(el).find(k => k.startsWith('__reactFiber$'))];" +
                                "var curr = fiber;" +
                                "var dayjsFn = window.dayjs || window.moment;" +
                                "var dtVal = dayjsFn ? dayjsFn(val) : new Date(val);" +
                                "while (curr) {" +
                                "  if (curr.memoizedProps && curr.memoizedProps.onChange) {" +
                                "    try { curr.memoizedProps.onChange(dtVal); } catch(e){}" +
                                "  }" +
                                "  curr = curr.return;" +
                                "}" +
                                "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set.call(el, val);"
                                +
                                "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                                "el.dispatchEvent(new Event('change', { bubbles: true }));",
                        el, startTime);
            } else {
                setReactInputValue(el, "");
            }
        }
        if (endTime != null) {
            WebElement el = waitForVisible(popupEndTimeInput);
            if (!endTime.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript(
                        "var el = arguments[0];" +
                                "var val = arguments[1];" +
                                "var fiber = el[Object.keys(el).find(k => k.startsWith('__reactFiber$'))];" +
                                "var curr = fiber;" +
                                "var dayjsFn = window.dayjs || window.moment;" +
                                "var dtVal = dayjsFn ? dayjsFn(val) : new Date(val);" +
                                "while (curr) {" +
                                "  if (curr.memoizedProps && curr.memoizedProps.onChange) {" +
                                "    try { curr.memoizedProps.onChange(dtVal); } catch(e){}" +
                                "  }" +
                                "  curr = curr.return;" +
                                "}" +
                                "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set.call(el, val);"
                                +
                                "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                                "el.dispatchEvent(new Event('change', { bubbles: true }));",
                        el, endTime);
            } else {
                setReactInputValue(el, "");
            }
        }
        if (notes != null) {
            WebElement el = waitForVisible(popupNotesTextarea);
            setReactInputValue(el, notes);
        }
    }

    public void resetPopup() {
        WebElement btn = waitForClickable(popupResetButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        try {
            Thread.sleep(500);
        } catch (Exception ignored) {
        }
    }

    public void submitPopup() {
        WebElement btn = waitForClickable(popupSubmitButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        try {
            Thread.sleep(2500);
        } catch (Exception ignored) {
        }
    }

    public boolean isValidationErrorShown() {
        try {
            return waitForVisible(validationErrorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationErrorMessage() {
        try {
            List<WebElement> errors = driver.findElements(validationErrorMessage);
            StringBuilder sb = new StringBuilder();
            for (WebElement err : errors) {
                if (err.isDisplayed()) {
                    sb.append(err.getText()).append(" ");
                }
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return getText(validationErrorMessage);
        }
    }

    public String getStartTimeValue() {
        return waitForVisible(popupStartTimeInput).getDomProperty("value");
    }

    public String getEndTimeValue() {
        return waitForVisible(popupEndTimeInput).getDomProperty("value");
    }

    public String getNotesValue() {
        return waitForVisible(popupNotesTextarea).getDomProperty("value");
    }
}
