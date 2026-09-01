package id.co.juaracoding.hadir.pages.employee;

import id.co.juaracoding.hadir.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk halaman Absensi Employee (Absen Masuk) aplikasi HADIR.
 * Menangani seluruh interaksi pada halaman /apps/absent dan modal Absen Masuk.
 */
public class AttendancePage extends BasePage {

    // --- Lokator halaman utama /apps/absent ---

    // Tombol "Absen Masuk" pada halaman utama absensi employee
    private final By absenMasukButton = By.xpath("//button[contains(., 'Absen Masuk') and not(ancestor::div[@role='dialog']) and not(ancestor::div[contains(@class, 'MuiDrawer-paper')])]");

    // Tombol "Keluar" / "Absen Keluar" pada kartu absensi
    private final By keluarButton = By.xpath("//button[(contains(., 'Absen Keluar') or contains(., 'Keluar')) and not(ancestor::header) and not(ancestor::nav)]");

    // Elemen pemilih bulan yang menandakan halaman absensi sudah dimuat
    private final By monthButton = By.id("month");

    // Heading section "History Absensi" — muncul setelah React selesai memuat data riwayat absensi dari API.
    // Digunakan sebagai sinyal bahwa halaman sepenuhnya siap (termasuk async history records).
    private final By historyAbsensiSection = By.xpath("//*[contains(normalize-space(text()), 'History Absensi')]");

    // --- Lokator modal Absen Masuk ---

    // Tombol shutter kamera pada modal foto absensi (step 1)
    private final By cameraShutterButton = By.xpath("//button[.//*[local-name()='svg' and contains(@class, 'feather-camera')]]");

    // Kolom catatan/notes pada modal Absen Masuk (step 2 form)
    private final By catatanField = By.cssSelector("input[name='notes'], form textarea, form input[type='text']");

    // Tombol final submit Absen Masuk di dalam form modal (step 2 form)
    private final By submitAbsenMasukButton = By.cssSelector("form button[type='submit']");

    // Backdrop overlay untuk menutup modal Absen Masuk
    private final By modalBackdrop = By.cssSelector("div.MuiBackdrop-root");

    // Dialog/modal container untuk memverifikasi modal telah muncul
    private final By attendanceModal = By.xpath("//div[@role='dialog'] | //div[contains(@class, 'MuiDrawer-paper')] | //form");

    // --- Lokator pesan respon (Toast / Dialog Alert) ---

    // Toast pesan sukses (misal: "Absen masuk berhasil")
    private final By successMessage = By.xpath("//*[contains(text(), 'Absen masuk berhasil') or contains(text(), 'berhasil')] | //div[contains(@class, 'MuiAlert-message')] | //div[@role='alert'] | //div[contains(@class, 'MuiSnackbar-root')]");

    // Dialog / pesan error kamera di DOM
    private final By cameraErrorDialog = By.xpath("//*[contains(translate(text(), 'KAMERA', 'kamera'), 'kamera') or contains(text(), 'NotAllowedError') or contains(text(), 'Permission denied')]");

    // Dialog / pesan error lokasi di DOM
    private final By locationErrorDialog = By.xpath("//*[contains(translate(text(), 'LOKASI', 'lokasi'), 'lokasi') or contains(translate(text(), 'LOCATION', 'location'), 'location') or contains(text(), 'User denied Geolocation')]");

    // Pesan validasi dialog "Anda belum melakukan absen keluar"
    // Locator di-scope ke dalam div[@role='dialog'] berdasarkan bukti aktual DOM MCP Playwright:
    // <div role="dialog" class="...MuiDialog-paper..."><div class="MuiDialogContent-root"><p>Anda belum...</p></div></div>
    private final By belumAbsenKeluarMessage = By.xpath("//div[@role='dialog']//p[contains(text(), 'Anda belum melakukan absen keluar')]");

    // Kartu / record absensi yang memiliki status masuk (IN) namun belum checkout (waktu keluar '- -').
    // PENTING: Elemen <p> memiliki multiple text nodes (React memecah teks ke beberapa node):
    //   "Masuk pukul " | "04:16" | " -" | " " | "-"
    // XPath text() hanya cocok dengan SATU text node sekaligus, sehingga contains(text(), '- -') tidak pernah match.
    // Gunakan titik (.) yang mengevaluasi seluruh string value elemen (semua text nodes digabungkan).
    private final By openAttendanceRecord = By.xpath("//p[contains(normalize-space(.), 'Masuk pukul') and contains(., '- -')]");

    /**
     * Konstruktor AttendancePage yang menginisialisasi WebDriver melalui superclass BasePage.
     *
     * @param driver instansi WebDriver yang digunakan
     */
    public AttendancePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Membuka halaman utama absensi employee (/apps/absent) dan menunggu halaman selesai dimuat.
     */
    public void openAttendancePage() {
        String absentUrl = "https://magang.dikahadir.com/apps/absent";
        if (!getCurrentUrl().contains("/apps/absent")) {
            navigateTo(absentUrl);
        }
        // Tunggu statistik bulanan (monthButton) dan kemudian section "History Absensi" agar
        // async data riwayat yang di-fetch React juga sudah ada di DOM sebelum precondition check.
        waitForVisible(monthButton);
        waitForVisible(historyAbsensiSection);
    }

    /**
     * Menekan tombol "Absen Masuk" pada halaman utama absensi setelah memastikan tombol clickable.
     * Dilindungi dari StaleElementReferenceException akibat React DOM re-render dengan retry
     * menggunakan By locator sebagai sumber pencarian ulang pada setiap percobaan.
     */
    public void clickAbsenMasuk() {
        int maxRetries = 3;

        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement button = waitForClickable(absenMasukButton);
                button.click();
                demoDelay();
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException stale) {
                if (i == maxRetries - 1) {
                    throw stale;
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while recovering from stale element", e);
                }
            }
        }
    }

    /**
     * Memeriksa apakah tombol "Absen Masuk" tampil di layar menggunakan non-blocking findElements.
     *
     * @return true jika tombol Absen Masuk tersedia, false jika tidak
     */
    public boolean isAbsenMasukVisible() {
        try {
            java.util.List<WebElement> elements = driver.findElements(absenMasukButton);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah tombol "Keluar" tampil di layar setelah absensi berhasil.
     *
     * @return true jika tombol Keluar tersedia, false jika tidak
     */
    public boolean isKeluarButtonVisible() {
        try {
            java.util.List<WebElement> elements = driver.findElements(keluarButton);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah pengguna sudah melakukan Absen Masuk untuk hari ini.
     * Kriteria: tombol "Absen Masuk" tidak tampil di layar.
     * Tidak bergantung pada keluarButton karena tombol Keluar juga hadir di kartu
     * History Absensi hari-hari sebelumnya sehingga tidak bisa dijadikan indikator.
     *
     * @return true jika sudah absen masuk hari ini, false jika belum
     */
    public boolean hasAlreadyCheckedInToday() {
        boolean absenMasukVis = isAbsenMasukVisible();
        boolean alreadyCheckedIn = !absenMasukVis;

        System.out.println("[DEBUG] Absen Masuk visible  : " + absenMasukVis);
        System.out.println("[DEBUG] Already checked in   : " + alreadyCheckedIn);

        return alreadyCheckedIn;
    }

    /**
     * Memeriksa apakah pengguna memiliki record absensi yang memiliki status masuk (IN)
     * namun belum checkout (waktu keluar bernilai '- -').
     * Digunakan sebagai precondition untuk Positive Test 2 (STATE 2).
     *
     * @return true jika terdapat attendance yang belum checkout, false jika tidak
     */
    public boolean hasAttendanceBelumCheckout() {
        try {
            // Gunakan wait eksplisit karena History Absensi dimuat secara async oleh React/API.
            // findElements() langsung tanpa wait akan mengembalikan list kosong sebelum DOM selesai dirender.
            WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(openAttendanceRecord)
            );
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah pesan validasi drawer/dialog "Anda belum melakukan absen keluar"
     * tampil di layar setelah menekan tombol Absen Masuk pada STATE 2.
     *
     * @return true jika pesan validasi tampil, false jika tidak
     */
    public boolean isBelumAbsenKeluarValidationVisible() {
        try {
            WebElement messageElement = waitForVisible(belumAbsenKeluarMessage);
            return messageElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memeriksa apakah halaman utama absensi (dashboard employee) sudah dimuat.
     *
     * @return true jika halaman absensi sudah tampil, false jika tidak
     */
    public boolean isAttendancePageLoaded() {
        return isDisplayed(monthButton);
    }

    /**
     * Memeriksa apakah modal Absen Masuk (dialog kamera atau drawer formulir) sudah muncul di layar.
     *
     * @return true jika modal sudah tampil, false jika tidak
     */
    public boolean isAttendanceModalVisible() {
        try {
            WebElement modal = waitForVisible(attendanceModal);
            return modal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Mengisi kolom catatan/notes pada modal Absen Masuk.
     * Jika kamera shutter masih tampil, menekan shutter kamera terlebih dahulu agar formulir catatan terbuka.
     *
     * @param catatan teks catatan yang akan diisi
     */
    public void fillCatatan(String catatan) {
        try {
            java.util.List<WebElement> shutters = driver.findElements(cameraShutterButton);
            if (!shutters.isEmpty()) {
                try {
                    if (shutters.get(0).isDisplayed()) {
                        shutters.get(0).click();
                    }
                } catch (Exception ignored) {
                }
            }
            WebElement element = waitForVisible(catatanField);
            element.clear();
            element.sendKeys(catatan);
            demoDelay();
        } catch (Exception e) {
            System.err.println("Peringatan: Kolom catatan tidak tersedia: " + e.getMessage());
        }
    }

    /**
     * Menekan tombol final submit "Absen Masuk" pada modal absensi.
     * Mengambil foto kamera terlebih dahulu jika shutter kamera masih aktif, kemudian menekan tombol submit pada formulir.
     */
    public void clickSubmitAbsenMasuk() {
        java.util.List<WebElement> shutters = driver.findElements(cameraShutterButton);
        if (!shutters.isEmpty()) {
            try {
                if (shutters.get(0).isDisplayed()) {
                    shutters.get(0).click();
                }
            } catch (Exception ignored) {
            }
        }

        try {
            WebElement button = waitForVisible(submitAbsenMasukButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
            demoDelay();
            try {
                waitForClickable(submitAbsenMasukButton).click();
            } catch (Exception clickEx) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
        } catch (Exception e) {
            try {
                WebElement button = driver.findElement(submitAbsenMasukButton);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            } catch (Exception ex) {
                click(submitAbsenMasukButton);
            }
        }
    }

    /**
     * Menutup modal Absen Masuk dengan menekan tombol Escape.
     */
    public void closeAttendanceModal() {
        try {
            driver.findElement(By.tagName("body")).sendKeys(
                    org.openqa.selenium.Keys.ESCAPE
            );
            demoDelay();
        } catch (Exception e) {
            if (isDisplayed(modalBackdrop)) {
                click(modalBackdrop);
            }
        }
    }

    /**
     * Melakukan alur lengkap Absen Masuk:
     * membuka modal, mengisi catatan opsional, dan menekan tombol submit.
     *
     * @param catatan teks catatan absensi; boleh kosong ("") jika tidak diperlukan
     */
    public void doAbsenMasuk(String catatan) {
        clickAbsenMasuk();
        wait.until(ExpectedConditions.visibilityOfElementLocated(attendanceModal));
        if (catatan != null && !catatan.trim().isEmpty()) {
            fillCatatan(catatan);
        }
        clickSubmitAbsenMasuk();
    }

    /**
     * Menunggu proses Absen Masuk selesai dan UI terupdate.
     * Kondisi tunggu: tombol "Absen Masuk" tidak lagi tampil di layar.
     * Menggunakan invisibility saja (bukan OR) untuk menghindari race condition:
     * kondisi OR sebelumnya terpenuhi seketika karena tombol "Keluar" di kartu
     * History Absensi hari-hari sebelumnya selalu visible sejak halaman dimuat.
     */
    public void waitForAbsenMasukBerhasil() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(absenMasukButton));
    }

    /**
     * Mengambil teks pesan sukses yang tampil pada Toast Notification (opsional / transient).
     *
     * @return teks pesan sukses
     */
    public String getSuccessMessage() {
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return messageElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Mengambil teks pesan kesalahan kamera dari pop-up alert JavaScript bawaan browser
     * atau dari elemen dialog DOM.
     *
     * @return teks pesan error kamera
     */
    public String getCameraErrorMessage() {
        try {
            org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception e) {
            try {
                return waitForVisible(cameraErrorDialog).getText();
            } catch (Exception ex) {
                try {
                    return waitForVisible(successMessage).getText();
                } catch (Exception ex2) {
                    return "";
                }
            }
        }
    }

    /**
     * Mengambil teks pesan kesalahan lokasi dari pop-up alert JavaScript bawaan browser
     * atau dari elemen dialog DOM.
     *
     * @return teks pesan error lokasi
     */
    public String getLocationErrorMessage() {
        try {
            org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception e) {
            try {
                return waitForVisible(locationErrorDialog).getText();
            } catch (Exception ex) {
                try {
                    return waitForVisible(successMessage).getText();
                } catch (Exception ex2) {
                    return "";
                }
            }
        }
    }
}
