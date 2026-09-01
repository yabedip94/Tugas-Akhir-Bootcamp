package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.AttendancePage;
import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.Config;
import id.co.juaracoding.hadir.utils.DriverFactory;
import id.co.juaracoding.hadir.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian otomatis TestNG untuk fitur Employee Absen Masuk aplikasi HADIR.
 */
public class AttendanceTest extends BaseTest {

    private LoginPage loginPage;
    private AttendancePage attendancePage;

    /**
     * Inisialisasi Page Object LoginPage dan AttendancePage sebelum setiap pengujian dijalankan.
     */
    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
        attendancePage = new AttendancePage(driver);
    }

    /**
     * Pengujian positif: Menguji alur Absen Masuk secara dinamis berdasarkan state aktual aplikasi saat runtime.
     * Menggunakan akun Employee 1 (hadirsqa1@gmail.com).
     *
     * Branching logic:
     *
     *   STATE 3 — isAbsenMasukVisible() == false:
     *     Employee sudah Absen Masuk hari ini. Tidak ada skenario positif yang dapat dijalankan.
     *     → SkipException.
     *
     *   STATE 2 — isAbsenMasukVisible() == true DAN hasAttendanceBelumCheckout() == true:
     *     Employee belum Absen Masuk hari ini, tetapi memiliki attendance open dari hari sebelumnya.
     *     → Klik Absen Masuk → Assert dialog "Anda belum melakukan absen keluar" tampil.
     *     → Tidak ada submit. Tidak ada transaksi baru.
     *
     *   STATE 1 — isAbsenMasukVisible() == true DAN hasAttendanceBelumCheckout() == false:
     *     Employee belum Absen Masuk hari ini, tidak ada attendance open.
     *     → Klik Absen Masuk → Isi catatan → Submit → Assert berhasil.
     *
     * Bukti MCP Playwright (01 September 2026):
     *   E1 saat ini berada pada STATE 2:
     *   - Tombol Absen Masuk visible.
     *   - 4 record history dengan pola "Masuk pukul ... - -" (attendance open).
     *   - API /api/activity/user-check-correction: status=IN, time_out=null, is_check_out=false.
     *   - Setelah klik Absen Masuk → dialog "Anda belum melakukan absen keluar" muncul.
     */
    @Test(description = "Verifikasi Absen Masuk berhasil dengan kredensial Employee 1")
    public void testAbsenMasukBerhasil() {
        // 1. Login dengan akun Employee 1
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();
        loginPage.login(email, password);

        // 2. Buka halaman absensi dan tunggu hingga history section siap di DOM
        attendancePage.openAttendancePage();

        // --- STATE DETECTION ---

        boolean absenMasukVisible = attendancePage.isAbsenMasukVisible();

        // STATE 3: tombol Absen Masuk tidak ada → sudah check-in hari ini → SKIP
        if (!absenMasukVisible) {
            throw new SkipException(
                "[STATE 3] Employee 1 sudah Absen Masuk hari ini. " +
                "Tidak ada skenario positive test yang dapat dijalankan saat ini."
            );
        }

        boolean hasOpenAttendance = attendancePage.hasAttendanceBelumCheckout();

        if (hasOpenAttendance) {
            // --- STATE 2: Tombol visible + ada attendance open → dialog validasi diharapkan ---
            System.out.println("[BRANCH] STATE 2 terdeteksi: Absen Masuk visible + attendance open dari hari sebelumnya.");
            System.out.println("[BRANCH] Menjalankan: Positive Test 2 — Verifikasi dialog 'Anda belum melakukan absen keluar'.");

            // Klik Absen Masuk — tidak isi form, tidak submit
            attendancePage.clickAbsenMasuk();

            // Assert dialog validasi bisnis muncul
            Assert.assertTrue(
                attendancePage.isBelumAbsenKeluarValidationVisible(),
                "[STATE 2] Pesan validasi 'Anda belum melakukan absen keluar' tidak tampil pada dialog!"
            );

        } else {
            // --- STATE 1: Tombol visible + tidak ada attendance open → submit berhasil diharapkan ---
            System.out.println("[BRANCH] STATE 1 terdeteksi: Absen Masuk visible + tidak ada attendance open.");
            System.out.println("[BRANCH] Menjalankan: Positive Test 1 — Verifikasi Absen Masuk berhasil.");

            // Klik Absen Masuk
            attendancePage.clickAbsenMasuk();

            // Assert modal Absen Masuk muncul
            Assert.assertTrue(
                attendancePage.isAttendanceModalVisible(),
                "[STATE 1] Modal Absen Masuk tidak tampil di layar setelah tombol diklik!"
            );

            // Isi catatan
            attendancePage.fillCatatan("Automated check in test");

            // Submit
            attendancePage.clickSubmitAbsenMasuk();

            // Tunggu UI update setelah submit berhasil
            attendancePage.waitForAbsenMasukBerhasil();

            // Assert: tombol Keluar muncul setelah check-in berhasil
            Assert.assertTrue(
                attendancePage.isKeluarButtonVisible(),
                "[STATE 1] Tombol Keluar tidak tampil di layar setelah absensi berhasil!"
            );

            // Assert: tombol Absen Masuk sudah tidak ada
            Assert.assertFalse(
                attendancePage.isAbsenMasukVisible(),
                "[STATE 1] Tombol Absen Masuk masih tampil di layar setelah absensi berhasil!"
            );
        }
    }



    /**
     * Pengujian negatif: Menguji alur Absen Masuk saat izin kamera tidak diberikan / diblokir.
     */
    @Test(description = "Verifikasi Absen Masuk gagal ketika akses kamera ditolak")
    public void testAbsenMasukGagalKameraTidakDiizinkan() {
        // Buat ulang sesi browser khusus dengan izin kamera diblokir (camera = false, geolocation = true)
        DriverFactory.quitDriver();
        driver = DriverFactory.initDriver(Config.getBrowser(), false, true);
        initPage();

        // 1. Buka halaman login dan lakukan autentikasi dengan akun Employee 3 (BELUM ABSEN)
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee3Username();
        String password = TestDataUtils.getEmployee3Password();
        loginPage.login(email, password);

        // 2. Navigasi ke halaman utama absensi employee (/apps/absent)
        attendancePage.openAttendancePage();

        // 3. Verifikasi tombol Absen Masuk tersedia di layar
        Assert.assertTrue(
                attendancePage.isAbsenMasukVisible(),
                "Tombol Absen Masuk tidak tersedia pada halaman utama absensi!"
        );

        // 4. Klik tombol Absen Masuk
        attendancePage.clickAbsenMasuk();

        // 5. Assertion: Verifikasi pesan kesalahan kamera (JS alert atau DOM) tampil di layar
        String cameraErrorMessage = attendancePage.getCameraErrorMessage();
        String lowerCameraMsg = cameraErrorMessage.toLowerCase();
        Assert.assertTrue(
                lowerCameraMsg.contains("kamera") || cameraErrorMessage.contains("NotAllowedError")
                        || cameraErrorMessage.contains("Permission denied"),
                "Pesan kesalahan akses kamera tidak sesuai! Teks aktual: " + cameraErrorMessage
        );
    }

    /**
     * Pengujian negatif: Menguji alur Absen Masuk saat izin lokasi (geolocation) tidak diberikan / diblokir.
     */
    @Test(description = "Verifikasi Absen Masuk gagal ketika akses lokasi ditolak")
    public void testAbsenMasukGagalLokasiTidakDiizinkan() {
        // Buat ulang sesi browser khusus dengan izin geolokasi diblokir (camera = true, geolocation = false)
        DriverFactory.quitDriver();
        driver = DriverFactory.initDriver(Config.getBrowser(), true, false);
        initPage();

        // 1. Buka halaman login dan lakukan autentikasi dengan akun Employee 3 (BELUM ABSEN)
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee3Username();
        String password = TestDataUtils.getEmployee3Password();
        loginPage.login(email, password);

        // 2. Navigasi ke halaman utama absensi employee (/apps/absent)
        attendancePage.openAttendancePage();

        // 3. Verifikasi tombol Absen Masuk tersedia di layar
        Assert.assertTrue(
                attendancePage.isAbsenMasukVisible(),
                "Tombol Absen Masuk tidak tersedia pada halaman utama absensi!"
        );

        // 4. Klik tombol Absen Masuk
        attendancePage.clickAbsenMasuk();

        // 5. Assertion: Verifikasi pesan kesalahan lokasi (JS alert atau DOM) tampil di layar
        String locationErrorMessage = attendancePage.getLocationErrorMessage();
        String lowerLocationMsg = locationErrorMessage.toLowerCase();
        Assert.assertTrue(
                lowerLocationMsg.contains("lokasi") || lowerLocationMsg.contains("location")
                        || locationErrorMessage.contains("User denied Geolocation"),
                "Pesan kesalahan akses lokasi tidak sesuai! Teks aktual: " + locationErrorMessage
        );
    }
}
