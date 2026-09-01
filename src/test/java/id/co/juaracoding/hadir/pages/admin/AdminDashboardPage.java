package id.co.juaracoding.hadir.pages.admin;

import id.co.juaracoding.hadir.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object Model untuk halaman Dashboard Admin aplikasi HADIR.
 *
 * Flow:
 * Login Admin
 * -> Dashboard Admin
 * -> Management
 * -> Pendaftaran User
 * -> Halaman Pendaftaran User
 */
public class AdminDashboardPage extends BasePage {

    // Locator verifikasi Dashboard Admin berdasarkan DOM aktual
    private final By adminProfile = By.xpath("//h5[normalize-space()='Admin Hadir']");

    // Locator menu Management berdasarkan DOM aktual
    private final By managementMenu = By.xpath("//p[normalize-space()='Management']");

    // Locator submenu Pendaftaran User berdasarkan DOM aktual
    private final By pendaftaranUserSubmenu = By.xpath("//p[normalize-space()='Pendaftaran User']");

    // Locator verifikasi halaman Pendaftaran User
    private final By registrasiUserHeading = By.xpath("//h1[normalize-space()='Registrasi User']");

    /**
     * Konstruktor AdminDashboardPage.
     *
     * @param driver WebDriver yang digunakan
     */
    public AdminDashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Memeriksa apakah Dashboard Admin berhasil tampil.
     *
     * Element yang digunakan adalah profile "Admin Hadir"
     * yang terkonfirmasi dari hasil inspeksi DOM aktual.
     *
     * @return true jika Dashboard Admin tampil
     */
    public boolean isAdminDashboardDisplayed() {
        return isDisplayed(adminProfile);
    }

    /**
     * Membuka menu Management.
     *
     * Menu Management merupakan accordion/collapse,
     * sehingga setelah diklik perlu menunggu submenu
     * Pendaftaran User menjadi visible.
     */
    public void clickManagement() {
        click(managementMenu);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pendaftaranUserSubmenu));
    }

    /**
     * Membuka menu Pendaftaran User.
     *
     * Setelah diklik, aplikasi melakukan client-side
     * navigation menuju halaman Pendaftaran User.
     */
    public void clickPendaftaranUser() {
        click(pendaftaranUserSubmenu);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        registrasiUserHeading));
    }

    /**
     * Memeriksa apakah halaman Pendaftaran User berhasil dibuka.
     *
     * Verifikasi menggunakan heading utama:
     * "Registrasi User"
     *
     * @return true jika halaman Pendaftaran User tampil
     */
    public boolean isPendaftaranUserPageDisplayed() {
        return isDisplayed(registrasiUserHeading);
    }
}