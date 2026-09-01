package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.AttendanceHistoryPage;
import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * TestNG automation untuk fitur Employee Attendance History
 * pada aplikasi HADIR.
 */
public class AttendanceHistoryTest extends BaseTest {

        private LoginPage loginPage;
        private AttendanceHistoryPage attendanceHistoryPage;

        @BeforeMethod
        public void initPage() {
                loginPage = new LoginPage(driver);
                attendanceHistoryPage = new AttendanceHistoryPage(driver);
        }

        /**
         * Positive test:
         * Memverifikasi employee dapat membuka History Absensi
         * melalui menu Absensi dan melihat record history
         * dengan informasi yang dapat ditampilkan.
         */
        @Test(description = "Verifikasi Employee dapat membuka History Absensi dan melihat record riwayat absensi")
        public void testAksesDanTampilanAttendanceHistory() {

                // 1. Login menggunakan Employee 1 yang memiliki history attendance
                loginPage.openLoginPage();

                String email = TestDataUtils.getEmployee1Username();
                String password = TestDataUtils.getEmployee1Password();

                loginPage.login(email, password);

                // 2. Buka History Absensi melalui menu Absensi
                attendanceHistoryPage.clickMenuAbsensi();

                // 3. Tunggu halaman History Absensi siap
                attendanceHistoryPage.waitForPageReady();

                // 4. Verifikasi URL
                Assert.assertTrue(
                                attendanceHistoryPage.getCurrentUrl().contains("/apps/absent/activity"),
                                "URL halaman History Absensi tidak sesuai: "
                                                + attendanceHistoryPage.getCurrentUrl());

                // 5. Verifikasi judul halaman
                Assert.assertEquals(
                                attendanceHistoryPage.getPageTitleText(),
                                "History Absensi",
                                "Judul halaman History Absensi tidak sesuai!");

                // 6. Verifikasi terdapat minimal satu record history
                List<WebElement> cards = attendanceHistoryPage.getHistoryCards();

                Assert.assertFalse(
                                cards.isEmpty(),
                                "History Absensi tidak menampilkan record attendance!");

                // 7. Verifikasi kelengkapan informasi pada record pertama
                WebElement firstCard = cards.get(0);

                String name = attendanceHistoryPage.getEmployeeName(firstCard);
                String date = attendanceHistoryPage.getAttendanceDate(firstCard);
                String type = attendanceHistoryPage.getAttendanceType(firstCard);
                String time = attendanceHistoryPage.getAttendanceTime(firstCard);
                String notes = attendanceHistoryPage.getNotes(firstCard);

                Assert.assertFalse(
                                name.trim().isEmpty(),
                                "Nama employee pada record pertama tidak boleh kosong!");

                Assert.assertFalse(
                                date.trim().isEmpty(),
                                "Tanggal attendance pada record pertama tidak boleh kosong!");

                Assert.assertFalse(
                                type.trim().isEmpty(),
                                "Tipe/status attendance pada record pertama tidak boleh kosong!");

                Assert.assertFalse(
                                time.trim().isEmpty(),
                                "Waktu attendance pada record pertama tidak boleh kosong!");

                Assert.assertNotNull(
                                notes,
                                "Notes pada record pertama harus dapat diakses!");
        }
}