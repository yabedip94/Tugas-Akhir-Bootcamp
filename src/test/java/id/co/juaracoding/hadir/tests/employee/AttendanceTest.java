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
     * Pengujian positif: Menguji alur Absen Masuk berhasil menggunakan akun Employee 2.
     * Pengujian ini melakukan submit nyata ke server sebagai bagian dari alur testing.
     */
    @Test(description = "Verifikasi Absen Masuk berhasil dengan kredensial Employee 2")
    public void testAbsenMasukBerhasil() {
        // 1. Buka halaman login dan lakukan autentikasi dengan akun Employee 2
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee2Username();
        String password = TestDataUtils.getEmployee2Password();
        loginPage.login(email, password);

        // 2. Navigasi / pastikan berada pada halaman utama absensi employee (/apps/absent)
        attendancePage.openAttendancePage();

        // Precondition check: Cek apakah employee sudah melakukan Absen Masuk hari ini
        if (attendancePage.hasAlreadyCheckedInToday()) {
            throw new SkipException("Employee sudah melakukan Absen Masuk hari ini. Positive test dilewati.");
        }

        // 3. Verifikasi tombol Absen Masuk tersedia di layar
        Assert.assertTrue(
                attendancePage.isAbsenMasukVisible(),
                "Tombol Absen Masuk tidak tersedia pada halaman utama absensi!"
        );

        // 4. Klik tombol Absen Masuk untuk membuka modal absensi
        attendancePage.clickAbsenMasuk();

        // 5. Verifikasi modal Absen Masuk tampil di layar
        Assert.assertTrue(
                attendancePage.isAttendanceModalVisible(),
                "Modal Absen Masuk tidak tampil di layar setelah tombol diklik!"
        );

        // 6. Isi catatan absensi
        attendancePage.fillCatatan("Test Absen Masuk Positive");

        // 7. Tekan tombol final submit Absen Masuk (submit nyata)
        attendancePage.clickSubmitAbsenMasuk();

        // 8. Tunggu hingga UI selesai update dan absensi tercatat
        attendancePage.waitForAbsenMasukBerhasil();

        // 9. Assertion 1: Verifikasi tombol "Keluar" tampil di layar setelah absensi berhasil
        Assert.assertTrue(
                attendancePage.isKeluarButtonVisible(),
                "Tombol Keluar tidak tampil di layar setelah absensi berhasil!"
        );

        // 10. Assertion 2: Verifikasi tombol "Absen Masuk" sudah tidak tampil di layar
        Assert.assertFalse(
                attendancePage.isAbsenMasukVisible(),
                "Tombol Absen Masuk masih tampil di layar setelah absensi berhasil!"
        );
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
