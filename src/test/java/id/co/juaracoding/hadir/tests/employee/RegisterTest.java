package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.RegisterPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

/**
 * Pengujian otomatis TestNG untuk fitur Registrasi Employee aplikasi HADIR.
 * 
 * Test Suite disederhanakan menjadi 2 testcase utama dengan manual state control:
 * 1. Positive: Registrasi berhasil dengan data valid (Hanya jika REGISTER_TEST_STATE=REGISTER_NEW).
 * 2. Negative: Validasi format email tidak valid (HTML5 Client-Side Validation).
 */
public class RegisterTest extends BaseTest {

    private RegisterPage registerPage;
    private String fixtureSelfiePath;

    @BeforeMethod
    public void initPage() {
        registerPage = new RegisterPage(driver);
        fixtureSelfiePath = Paths.get("src", "test", "resources", "fixtures", "sample_selfie.png")
                .toAbsolutePath().toString();
    }

    /**
     * Test 1 (Positive): Menguji registrasi berhasil dengan data valid dari .env-hadir.
     * Dikontrol via REGISTER_TEST_STATE (Default: REGISTER_EXISTING -> SKIP test).
     */
    @Test(description = "Verifikasi registrasi berhasil dengan data valid")
    public void testRegisterWithAvailableNik() {

        String state = TestDataUtils.getRegisterTestState();

        if ("REGISTER_EXISTING".equalsIgnoreCase(state)) {
            System.out.println("[REGISTER] ==========================================");
            System.out.println("[REGISTER] Test : Positive Registration");
            System.out.println("[REGISTER] State: REGISTER_EXISTING");
            System.out.println("[REGISTER] Mode : Existing account / Safe mode");
            System.out.println("[REGISTER] ------------------------------------------");
            System.out.println("[REGISTER] Planned Registration Flow (REGISTER_NEW mode):");
            System.out.println("[REGISTER] 1. Prepare registration test data from .env-hadir");
            System.out.println("[REGISTER] 2. Check REGISTER_TEST_STATE value");
            System.out.println("[REGISTER] 3. Existing account detected");
            System.out.println("[REGISTER] 4. Positive registration execution skipped");
            System.out.println("[REGISTER] 5. No new account will be created");
            System.out.println("[REGISTER] ------------------------------------------");
            System.out.println("[REGISTER] Result: SKIPPED");
            System.out.println("[REGISTER] Reason: REGISTER_TEST_STATE=REGISTER_EXISTING");
            System.out.println("[REGISTER] Browser Action: NOT EXECUTED");
            System.out.println("[REGISTER] Account Creation: DISABLED");
            System.out.println("[REGISTER] ==========================================");

            throw new SkipException(
                    "Register positive test di-SKIP: REGISTER_TEST_STATE=REGISTER_EXISTING"
            );
        }

        if (!"REGISTER_NEW".equalsIgnoreCase(state)) {
            Assert.fail(
                    "REGISTER_TEST_STATE harus REGISTER_EXISTING atau REGISTER_NEW"
            );
        }

        // Ambil semua data dari env
        String nik = TestDataUtils.getRegisterNik();
        String fullname = TestDataUtils.getRegisterFullname();
        String email = TestDataUtils.getRegisterEmail();
        String password = TestDataUtils.getRegisterPassword();

        // Fail fast jika REGISTER_NEW tetapi data belum lengkap
        if (nik == null || nik.trim().isEmpty()) {
            Assert.fail("REGISTER_NIK wajib diisi ketika REGISTER_TEST_STATE=REGISTER_NEW");
        }

        if (fullname == null || fullname.trim().isEmpty()) {
            Assert.fail("REGISTER_FULLNAME wajib diisi ketika REGISTER_TEST_STATE=REGISTER_NEW");
        }

        if (email == null || email.trim().isEmpty()) {
            Assert.fail("REGISTER_EMAIL wajib diisi ketika REGISTER_TEST_STATE=REGISTER_NEW");
        }

        if (password == null || password.trim().isEmpty()) {
            Assert.fail("REGISTER_PASSWORD wajib diisi ketika REGISTER_TEST_STATE=REGISTER_NEW");
        }

        System.out.println("[REGISTER] ==========================================");
        System.out.println("[REGISTER] Test : Positive Registration");
        System.out.println("[REGISTER] State: REGISTER_NEW");
        System.out.println("[REGISTER] ------------------------------------------");
        System.out.println("[REGISTER] Step 1: Open Register Page");
        registerPage.openRegisterPage();

        System.out.println("[REGISTER] Step 2: Input NIK");
        registerPage.inputNik(nik);

        System.out.println("[REGISTER] Step 3: Input Fullname");
        registerPage.inputFullname(fullname);

        System.out.println("[REGISTER] Step 4: Input Email");
        registerPage.inputEmail(email);

        System.out.println("[REGISTER] Step 5: Input Password [PROTECTED]");
        registerPage.inputPassword(password);

        System.out.println("[REGISTER] Step 6: Upload Selfie");
        registerPage.uploadSelfie(fixtureSelfiePath);

        System.out.println("[REGISTER] Step 7: Submit Registration");
        registerPage.clickSubmit();

        System.out.println("[REGISTER] Step 8: Verify Success Alert");
        String alertText = registerPage.waitForRegistrationResult();

        Assert.assertTrue(
                alertText.toLowerCase().contains("berhasil register"),
                "Registrasi tidak berhasil. Actual alert: " + alertText
        );

        System.out.println("[REGISTER] Result: PASS");
        System.out.println("[REGISTER] Status: PENDING APPROVAL");
        System.out.println("[REGISTER] ==========================================");
    }

    /**
     * Test 2 (Negative): Verifikasi registrasi gagal jika format email tidak valid.
     * Tetap dijalankan meskipun REGISTER_TEST_STATE=REGISTER_EXISTING.
     * Browser Native HTML5 Email Validation memblokir submit pada client-side (tidak ada POST /users).
     */
    @Test(description = "Verifikasi registrasi gagal jika format email tidak valid")
    public void testRegisterInvalidEmailFormat() {

        String state = TestDataUtils.getRegisterTestState();
        if (state == null || state.trim().isEmpty()) {
            state = "REGISTER_EXISTING";
        }

        System.out.println("[REGISTER] ==========================================");
        System.out.println("[REGISTER] Test : Invalid Email Format");
        System.out.println("[REGISTER] State: " + state);
        System.out.println("[REGISTER] ------------------------------------------");

        String nik = TestDataUtils.getRegisterNik();
        if (nik == null || nik.trim().isEmpty()) {
            Assert.fail("REGISTER_NIK wajib diisi pada .env-hadir!");
        }

        String fullname = TestDataUtils.getRegisterFullname();
        if (fullname == null || fullname.trim().isEmpty()) {
            Assert.fail("REGISTER_FULLNAME wajib diisi pada .env-hadir!");
        }

        String password = TestDataUtils.getRegisterPassword();
        if (password == null || password.trim().isEmpty()) {
            Assert.fail("REGISTER_PASSWORD wajib diisi pada .env-hadir!");
        }

        System.out.println("[REGISTER] Step 1: Open Register Page");
        registerPage.openRegisterPage();

        System.out.println("[REGISTER] Step 2: Input NIK");
        registerPage.inputNik(nik);

        System.out.println("[REGISTER] Step 3: Input Fullname");
        registerPage.inputFullname(fullname);

        System.out.println("[REGISTER] Step 4: Input invalid email");
        registerPage.inputEmail("invalid-email");

        System.out.println("[REGISTER] Step 5: Input password [PROTECTED]");
        registerPage.inputPassword(password);

        System.out.println("[REGISTER] Step 6: Upload selfie");
        registerPage.uploadSelfie(fixtureSelfiePath);

        System.out.println("[REGISTER] Step 7: Submit registration form");
        registerPage.clickSubmit();

        System.out.println("[REGISTER] Step 8: Verify native email validation");
        // Assertion stabil berbasis DOM HTML5 Validation API (checkValidity() == false)
        Assert.assertFalse(
                registerPage.isEmailInputValid(),
                "HTML5 validation seharusnya menolak format email yang tidak valid!");

        // Memastikan submit dicegat client-side sehingga MUI Alert tidak pernah dipicu
        Assert.assertFalse(
                registerPage.isAlertDisplayed(),
                "MUI Alert tidak boleh muncul karena submit dicegat oleh HTML5 validation!");

        System.out.println("[REGISTER] Result: PASS");
        System.out.println("[REGISTER] ==========================================");
    }
}
