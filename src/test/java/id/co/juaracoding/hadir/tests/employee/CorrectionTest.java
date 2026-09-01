package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.CorrectionPage;
import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG automation untuk fitur Employee Koreksi Absen pada aplikasi HADIR.
 */
public class CorrectionTest extends BaseTest {

    private LoginPage loginPage;
    private CorrectionPage correctionPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
        correctionPage = new CorrectionPage(driver);
    }

    /**
     * Positive Test:
     * Memverifikasi employee dapat mengajukan koreksi absen dengan mengisi
     * Jam Masuk dan Jam Keluar yang valid.
     */
    @Test(description = "Verifikasi Employee berhasil mengajukan koreksi absen dengan Jam Masuk dan Jam Keluar valid")
    public void testAjukanKoreksiAbsenBerhasil() {
        System.out.println("[TIMING] 1. START testAjukanKoreksiAbsenBerhasil " + System.currentTimeMillis());

        // 1. Login menggunakan akun Employee 1
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();
        loginPage.login(email, password);

        // 2. Buka halaman Koreksi Absen melalui menu dashboard
        correctionPage.clickMenuKoreksiAbsen();

        // 3. Menunggu halaman Koreksi Absen selesai dimuat
        correctionPage.waitForCorrectionPageReady();

        // 4. Verifikasi URL dan Header Halaman Koreksi
        Assert.assertTrue(
                correctionPage.getCurrentUrl().contains("/apps/absent/correction"),
                "URL halaman Koreksi Absen tidak sesuai: " + correctionPage.getCurrentUrl()
        );
        Assert.assertEquals(
                correctionPage.getPageTitleText(),
                "Halaman Koreksi",
                "Judul halaman Koreksi Absen tidak sesuai!"
        );
        Assert.assertTrue(
                correctionPage.isListKoreksiVisible(),
                "Header List Koreksi tidak tampil di halaman!"
        );

        // 5. Buka modal formulir "Ajukan Koreksi"
        System.out.println("[TIMING] 2. Sebelum membuka form Koreksi " + System.currentTimeMillis());
        correctionPage.clickAjukanKoreksi();
        correctionPage.waitForModalVisible();
        Assert.assertTrue(
                correctionPage.isModalVisible(),
                "Modal Ajukan Koreksi Absen tidak tampil setelah tombol diklik!"
        );
        System.out.println("[TIMING] 3. Setelah form terbuka " + System.currentTimeMillis());

        // 6. Isi Jam Masuk, Jam Keluar, dan Tipe Absen
        System.out.println("[TIMING] 4. Sebelum mulai mengisi Jam Masuk " + System.currentTimeMillis());
        correctionPage.selectJamMasukFromPicker();
        System.out.println("[TIMING] 5. Setelah Jam Masuk selesai " + System.currentTimeMillis());
        correctionPage.selectJamKeluarFromPicker();
        System.out.println("[TIMING] 6. Setelah Jam Keluar selesai " + System.currentTimeMillis());
        correctionPage.selectTipeAbsen("wfh");
        System.out.println("[TIMING] 7. Setelah Tipe Absen selesai " + System.currentTimeMillis());

        // 7. Verifikasi kedua kolom input dan Tipe Absen telah terisi nilai
        String jamMasukVal = correctionPage.getJamMasukValue();
        String jamKeluarVal = correctionPage.getJamKeluarValue();
        String tipeAbsenVal = correctionPage.getTipeAbsenValue();

        Assert.assertFalse(
                jamMasukVal.trim().isEmpty(),
                "Kolom Jam Masuk tidak boleh kosong setelah dipilih dari picker!"
        );
        Assert.assertFalse(
                jamKeluarVal.trim().isEmpty(),
                "Kolom Jam Keluar tidak boleh kosong setelah dipilih dari picker!"
        );
        Assert.assertEquals(
                "WFH",
                tipeAbsenVal,
                "Tipe Absen harus terpilih sebagai WFH"
        );

        // 8. Submit formulir pengajuan koreksi
        System.out.println("[TIMING] 8. Tepat SEBELUM clickAjukan() " + System.currentTimeMillis());
        correctionPage.clickSubmitAjukan();
        System.out.println("[TIMING] 9. Tepat SETELAH clickAjukan() return " + System.currentTimeMillis());

        // 9. Tunggu dan verifikasi modal tertutup menandakan submission berhasil
        System.out.println("[TIMING] 10. Tepat SEBELUM assertion/verifikasi hasil submit " + System.currentTimeMillis());
        correctionPage.waitForModalClosed();
        System.out.println("[TIMING] 11. Tepat SETELAH assertion " + System.currentTimeMillis());
        System.out.println("[TIMING] 12. END test " + System.currentTimeMillis());
    }

    /**
     * Negative Test:
     * Memverifikasi employee gagal mengajukan koreksi absen ketika
     * Jam Masuk dan Jam Keluar dibiarkan kosong.
     */
    @Test(description = "Verifikasi Employee gagal mengajukan koreksi absen saat Jam Masuk dan Jam Keluar kosong")
    public void testAjukanKoreksiAbsenGagalFieldKosong() {
        // 1. Login menggunakan akun Employee 1
        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();
        loginPage.login(email, password);

        // 2. Buka halaman Koreksi Absen melalui menu dashboard
        correctionPage.clickMenuKoreksiAbsen();

        // 3. Menunggu halaman Koreksi Absen selesai dimuat
        correctionPage.waitForCorrectionPageReady();

        // 4. Buka modal formulir "Ajukan Koreksi"
        correctionPage.clickAjukanKoreksi();
        correctionPage.waitForModalVisible();
        Assert.assertTrue(
                correctionPage.isModalVisible(),
                "Modal Ajukan Koreksi Absen tidak tampil setelah tombol diklik!"
        );

        // 5. Pastikan kolom Jam Masuk dan Jam Keluar kosong
        Assert.assertTrue(
                correctionPage.getJamMasukValue().trim().isEmpty(),
                "Kolom Jam Masuk harus kosong sebelum pengujian negatif!"
        );
        Assert.assertTrue(
                correctionPage.getJamKeluarValue().trim().isEmpty(),
                "Kolom Jam Keluar harus kosong sebelum pengujian negatif!"
        );

        // 6. Submit formulir dalam keadaan field kosong
        correctionPage.clickSubmitAjukan();

        // 7. Verifikasi pesan validasi error muncul
        Assert.assertTrue(
                correctionPage.isErrorMessageDisplayed(),
                "Pesan error validasi harus tampil saat submit formulir kosong!"
        );
        Assert.assertEquals(
                correctionPage.getErrorMessageText(),
                "Salah satu harus diisi!",
                "Teks pesan validasi error tidak sesuai!"
        );

        // 8. Verifikasi modal tetap terbuka dan URL tidak berpindah
        Assert.assertTrue(
                correctionPage.isModalVisible(),
                "Modal formulir koreksi absen harus tetap terbuka saat validasi gagal!"
        );
        Assert.assertTrue(
                correctionPage.getCurrentUrl().contains("/apps/absent/correction"),
                "URL tidak boleh berpindah saat validasi gagal!"
        );
    }
}
