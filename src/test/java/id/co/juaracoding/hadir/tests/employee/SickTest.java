package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.LoginPage;
import id.co.juaracoding.hadir.pages.employee.SickPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test cases for the "Sick" module (Sakit).
 * Converted from SakitMobileTest reference implementation.
 */
public class SickTest extends BaseTest {

    private LoginPage loginPage;
    private SickPage sickPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
        sickPage = new SickPage(driver);

        loginPage.openLoginPage();
        String email = TestDataUtils.getEmployee1Username();
        String password = TestDataUtils.getEmployee1Password();
        loginPage.login(email, password);
    }

    @Test(groups = "positive", description = "[POSITIVE] Klik icon sakit -> berhasil menampilkan halaman sakit")
    public void openSickMenu_shouldDisplaySickPage() {
        sickPage.openSickMenu();
        String title = sickPage.readSickTitle();
        System.out.println("JUDUL: [" + title + "]");
        Assert.assertEquals(title, "Halaman Sakit",
                "Halaman Sakit seharusnya menampilkan Judul 'Halaman Sakit', yang tampil: " + title);
    }

    @Test(groups = "known-defect", description = "[KNOWN-DEFECT] Mengisi form sakit dengan data sesuai -> mengajukan sakit")
    public void submitSickForm_validData_shouldSucceed() {
        sickPage.openSickMenu();
        String title = sickPage.readSickTitle();
        System.out.println("JUDUL: [" + title + "]");
        int totalInitial = sickPage.readTotalSickRequests();

        sickPage.openSickForm();
        sickPage.waitForSickFormDrawer();
        String drawerTitle = sickPage.readSickFormDrawerTitle();
        System.out.println("JUDUL DRAWER: [" + drawerTitle + "]");

        String sentDate = sickPage.selectSickDate(1);
        sickPage.uploadSickDocument();
        sickPage.waitForReuploadButton();
        sickPage.clickSubmitSick();

        sickPage.waitForTotalChanged(totalInitial);
        int totalFinal = sickPage.readTotalSickRequests();
        Assert.assertEquals(totalFinal, totalInitial + 1,
                "total awal: " + totalInitial + " Sementara total akhir: " + totalFinal);

        String savedDate = sickPage.readFirstEntryStartDate();
        Assert.assertTrue(savedDate.contains(sentDate),
                "Tanggal mulai yang tersimpan seharusnya mengandung " + sentDate
                        + ", yang tampil: " + savedDate);
    }

    @Test(groups = "known-defect", description = "[KNOWN-DEFECT] Mengajukan form sakit tanpa surat sakit -> ditolak")
    public void submitSickForm_withoutDocument_shouldBeRejected() {
        sickPage.openSickMenu();
        String title = sickPage.readSickTitle();
        System.out.println("JUDUL: [" + title + "]");
        int totalInitial = sickPage.readTotalSickRequests();

        sickPage.openSickForm();
        sickPage.waitForSickFormDrawer();
        String drawerTitle = sickPage.readSickFormDrawerTitle();
        System.out.println("JUDUL DRAWER: [" + drawerTitle + "]");

        sickPage.selectSickDate(2);
        sickPage.clickSubmitSick();

        int totalFinal = sickPage.readTotalSickRequests();
        Assert.assertEquals(totalFinal, totalInitial,
                "Pengajuan sakit tanpa surat sakit seharusnya ditolak. " + "total awal: " + totalInitial
                        + " Sementara total akhir: " + totalFinal);
    }
}
