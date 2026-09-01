package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.LeavePage;
import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test cases for the "Leave" module (Cuti).
 * Converted from CutiMobileTest reference implementation.
 */
public class LeaveTest extends BaseTest {

    private LoginPage loginPage;
    private LeavePage leavePage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
        leavePage = new LeavePage(driver);

        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();
        loginPage.login(email, password);
    }

    @Test(groups = "positive", description = "[POSITIVE] Klik menu cuti -> berhasil menampilkan halaman cuti")
    public void openLeaveMenu_shouldDisplayLeavePage() {
        leavePage.openLeaveMenu();
        String title = leavePage.readLeaveTitle();
        System.out.println("JUDUL: [" + title + "]");
        Assert.assertEquals(title, "Halaman Cuti",
                "Halaman Cuti seharusnya menampilkan Judul 'Halaman Cuti', yang tampil: " + title);
    }

    @Test(groups = "positive", description = "[POSITIVE] Mengisi form cuti dengan data sesuai -> berhasil diajukan")
    public void submitLeave_validData_shouldSucceed() {
        leavePage.openLeaveMenu();
        leavePage.openLeaveForm();
        leavePage.waitForLeaveFormDrawer();
        leavePage.selectLeaveType();
        leavePage.selectLeaveDates();
        String notes = leavePage.fillLeaveNotes("E2E Automated Cuti Submission");
        leavePage.clickSubmitLeave();

        Assert.assertTrue(leavePage.isLeaveEntryPresent(notes),
                "Pengajuan cuti seharusnya muncul di daftar dengan catatan: " + notes);
    }

    @Test(groups = {"negative", "known-defect"}, description = "[NEGATIVE] Mengisi form cuti dengan tanggal lampau -> ditolak")
    public void submitLeave_pastDate_shouldBeRejected() {
        leavePage.openLeaveMenu();
        leavePage.openLeaveForm();
        leavePage.waitForLeaveFormDrawer();
        leavePage.selectLeaveType();
        leavePage.selectPastLeaveDates();
        String notes = leavePage.fillLeaveNotes("E2E Automated Negatif Backdate Cuti");
        leavePage.clickSubmitLeave();

        Assert.assertTrue(leavePage.isLeaveEntryNotPresent(notes),
                "Pengajuan cuti dengan tanggal lampau seharusnya ditolak, "
                        + "tetapi entri ditemukan di daftar dengan catatan: " + notes);
    }
}
