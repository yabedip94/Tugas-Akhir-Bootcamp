package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the "Permission" module.
 *
 * IMPORTANT: This module has TWO separate sub-sections, navigated via
 * toggle buttons: "Terlambat" and "Pulang Cepat". Each has its own list
 * and its own "Ajukan" popup form (Tanggal, Jam, Keterangan).
 * See docs/izin-module-handbook.md for the reference UI structure.
 */
public class PermissionPage extends BasePage {

    // --- Top-level navigation ---
    private final By permissionMenuIcon = By
            .xpath("//a[(contains(@class,'user__menu__item') or contains(@class,'user_menu_item')) and .//p[normalize-space()='Izin']]"); // CONFIRMED

    private final By lateTabButton = By
            .xpath("//button[@role='tab' and normalize-space()='Terlambat']"); // CONFIRMED

    private final By earlyLeaveTabButton = By
            .xpath("//button[@role='tab' and normalize-space()='Pulang Cepat']"); // CONFIRMED

    // --- Late: list + floating action button ---
    private final By lateListTitle = By
            .xpath("//*[normalize-space()='List Izin Terlambat']"); // CONFIRMED

    private final By lateListTotal = By
            .xpath("//*[contains(text(),'Total :')]"); // CONFIRMED

    private final By submitLateButton = By
            .xpath("//button[normalize-space()='Ajukan Izin Terlambat']"); // CONFIRMED

    // --- Early Leave: list + floating action button ---
    private final By earlyLeaveListTitle = By
            .xpath("//*[normalize-space()='List Pulang Cepat']"); // CONFIRMED

    private final By earlyLeaveListTotal = By
            .xpath("//*[contains(text(),'Total :')]"); // CONFIRMED

    private final By submitEarlyLeaveButton = By
            .xpath("//button[normalize-space()='Ajukan Pulang Cepat']"); // CONFIRMED

    // --- Popup form (shared structure for both Late & Early Leave) ---
    private final By popupDateInput = By
            .xpath("//input[@placeholder='mm/dd/yyyy']"); // CONFIRMED

    private final By popupTimeInput = By
            .xpath("//input[@placeholder='14:00']"); // CONFIRMED

    private final By popupDescriptionTextarea = By
            .id("notes"); // CONFIRMED

    private final By popupResetButton = By
            .xpath("//button[normalize-space()='Reset']"); // CONFIRMED

    private final By popupSubmitButton = By
            .xpath("//button[@type='submit' and normalize-space()='Ajukan']"); // CONFIRMED

    // --- Feedback after submit ---
    private final By validationErrorMessage = By
            .xpath("//*[contains(@class,'MuiFormHelperText-root') and contains(@class,'Mui-error')]"); // CONFIRMED

    public PermissionPage(WebDriver driver) {
        super(driver);
    }

    // --- Navigation ---
    public void openPermissionMenu() {
        waitForVisible(
                By.xpath("//*[contains(text(),'Hai,')] | //a[contains(@href,'izin') or contains(@class,'menu')]"));

        for (int i = 0; i < 3; i++) {
            try {
                WebElement menu = waitForClickable(permissionMenuIcon);

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView(true);",
                        menu);

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();",
                        menu);

                waitForVisible(lateListTitle);
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

    public void goToLateTab() {
        WebElement tab = waitForClickable(lateTabButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                tab);

        waitForVisible(lateListTitle);
    }

    public void goToEarlyLeaveTab() {
        WebElement tab = waitForClickable(earlyLeaveTabButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                tab);

        waitForVisible(earlyLeaveListTitle);
    }

    // --- Late flow ---
    public void openLatePermissionForm() {
        WebElement button = waitForClickable(submitLateButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);",
                button);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                button);

        waitForVisible(popupDateInput);
    }

    public int getTotalLate() {
        String totalText = getText(lateListTotal);

        return Integer.parseInt(
                totalText.replaceAll("[^0-9]", ""));
    }

    public void submitLatePermission(
            String date,
            String time,
            String description) {

        openLatePermissionForm();
        fillPopupForm(date, time, description);
        submitPopup();
    }

    // --- Early Leave flow ---
    public void openEarlyLeavePermissionForm() {
        WebElement button = waitForClickable(submitEarlyLeaveButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);",
                button);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                button);

        waitForVisible(popupDateInput);
    }

    public int getTotalEarlyLeave() {
        String totalText = getText(earlyLeaveListTotal);

        return Integer.parseInt(
                totalText.replaceAll("[^0-9]", ""));
    }

    public void submitEarlyLeave(
            String date,
            String time,
            String description) {

        openEarlyLeavePermissionForm();
        fillPopupForm(date, time, description);
        submitPopup();
    }

    // --- Shared popup actions ---
    private void setReactInputValue(
            WebElement element,
            String value) {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        String script = "var elem = arguments[0];" +
                "var value = arguments[1];" +
                "var prototype = elem.tagName.toLowerCase() === 'textarea' ? window.HTMLTextAreaElement.prototype : window.HTMLInputElement.prototype;"
                +
                "var descriptor = Object.getOwnPropertyDescriptor(prototype, 'value');"
                +
                "if (descriptor && descriptor.set) { descriptor.set.call(elem, value); } else { elem.value = value; }"
                +
                "elem.dispatchEvent(new Event('input', { bubbles: true }));"
                +
                "elem.dispatchEvent(new Event('change', { bubbles: true }));";

        js.executeScript(
                script,
                element,
                value);
    }

    public void fillPopupForm(
            String date,
            String time,
            String description) {

        if (date != null) {
            WebElement element = waitForVisible(popupDateInput);

            setReactInputValue(
                    element,
                    date);
        }

        if (time != null) {
            WebElement element = waitForVisible(popupTimeInput);

            if (!time.isEmpty()) {

                ((JavascriptExecutor) driver).executeScript(
                        "var el = arguments[0];" +
                                "var val = arguments[1];" +
                                "var fiber = el[Object.keys(el).find(k => k.startsWith('__reactFiber$'))];"
                                +
                                "var curr = fiber;" +
                                "var dayjsFn = window.dayjs || window.moment;" +
                                "var timeVal = dayjsFn ? dayjsFn('2026-09-01T' + val + ':00') : new Date('2026-09-01T' + val + ':00');"
                                +
                                "while (curr) {" +
                                "  if (curr.memoizedProps && curr.memoizedProps.onChange) {" +
                                "    try { curr.memoizedProps.onChange(timeVal); } catch(e){}" +
                                "  }" +
                                "  curr = curr.return;" +
                                "}" +
                                "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set.call(el, val);"
                                +
                                "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                                "el.dispatchEvent(new Event('change', { bubbles: true }));",
                        element,
                        time);

            } else {

                setReactInputValue(
                        element,
                        "");
            }
        }

        if (description != null) {
            WebElement element = waitForVisible(popupDescriptionTextarea);

            setReactInputValue(
                    element,
                    description);
        }
    }

    public void resetPopup() {
        WebElement button = waitForClickable(popupResetButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                button);

        try {
            Thread.sleep(500);
        } catch (Exception ignored) {
        }
    }

    public void submitPopup() {
        WebElement button = waitForClickable(popupSubmitButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                button);

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
            java.util.List<WebElement> errors = driver.findElements(validationErrorMessage);

            StringBuilder message = new StringBuilder();

            for (WebElement error : errors) {
                if (error.isDisplayed()) {
                    message.append(error.getText()).append(" ");
                }
            }

            return message.toString().trim();

        } catch (Exception e) {
            return getText(validationErrorMessage);
        }
    }

    public boolean isPopupClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(popupDateInput));
        } catch (Exception e) {
            return false;
        }
    }

    public String getDateValue() {
        return waitForVisible(popupDateInput)
                .getDomProperty("value");
    }

    public String getTimeValue() {
        return waitForVisible(popupTimeInput)
                .getDomProperty("value");
    }

    public String getDescriptionValue() {
        return waitForVisible(popupDescriptionTextarea)
                .getDomProperty("value");
    }
}