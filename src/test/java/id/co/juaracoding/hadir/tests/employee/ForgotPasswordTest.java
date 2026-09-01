package id.co.juaracoding.hadir.tests.employee;

import id.co.juaracoding.hadir.pages.employee.ForgotPasswordPage;
import id.co.juaracoding.hadir.tests.BaseTest;
import id.co.juaracoding.hadir.utils.TestDataUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pengujian otomatis TestNG untuk fitur Forgot Password (Lupa Password) Employee HADIR.
 * 
 * Skenario Pengujian:
 * 1. Positive: Permintaan reset password dengan email registered yang valid.
 * 2. Negative: Permintaan reset password dengan format email tidak valid (Native HTML5 Validation).
 */
public class ForgotPasswordTest extends BaseTest {

    private ForgotPasswordPage forgotPasswordPage;

    @BeforeMethod
    public void initPage() {
        forgotPasswordPage = new ForgotPasswordPage(driver);
    }

    /**
     * Test 1 (Positive): Menguji pengajuan reset password dengan email terdaftar.
     *
     * Verifikasi utama (persistent setelah redirect):
     * - Browser redirect ke URL mengandung /absen/reset-password dan message=success
     * - Field OTP (id="otp") tampil pada halaman reset password
     *
     * Catatan UI: Success toast (.MuiAlert-filledSuccess) dengan teks
     * "Link reset password terkirim, silahkan cek inbox email" MEMANG muncul secara visual
     * setelah submit, namun tidak dapat di-assert secara hard karena aplikasi langsung
     * me-redirect ke halaman reset password sebelum Selenium sempat mendeteksi elemen tersebut.
     * Behavior sukses diverifikasi melalui URL parameter dan kehadiran field OTP yang persistent.
     */
    @Test(description = "Verifikasi pengajuan reset password berhasil dengan email registered")
    public void testForgotPasswordValidEmail() {

        String registeredEmail = TestDataUtils.getEmployee1Username();

        if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
            Assert.fail("USERNAME_EMPLOYEE_1 tidak ditemukan pada .env-hadir!");
        }

        System.out.println("[FORGOT_PASSWORD] ==========================================");
        System.out.println("[FORGOT_PASSWORD] Test : Positive Forgot Password");
        System.out.println("[FORGOT_PASSWORD] ------------------------------------------");
        System.out.println("[FORGOT_PASSWORD] Step 1: Open Login Page");
        forgotPasswordPage.openLoginPage();

        System.out.println("[FORGOT_PASSWORD] Step 2: Click 'Lupa password ?' link");
        forgotPasswordPage.clickForgotPasswordLink();

        System.out.println("[FORGOT_PASSWORD] Step 3: Input registered email");
        forgotPasswordPage.inputEmail(registeredEmail);

        System.out.println("[FORGOT_PASSWORD] Step 4: Click Submit button");
        forgotPasswordPage.clickSubmit();

        // Step 5 - Toast sukses (.MuiAlert-filledSuccess) muncul secara visual setelah submit,
        // namun aplikasi langsung redirect sebelum Selenium dapat mendeteksinya.
        // Assertion toast tidak dilakukan untuk menghindari flaky test akibat race condition.
        // Bukti visual tersedia pada dokumentasi diagnosis DOM aktual.
        System.out.println("[FORGOT_PASSWORD] Step 5: Submit berhasil - aplikasi redirect ke halaman reset password");

        System.out.println("[FORGOT_PASSWORD] Step 6: Verify Redirect URL");
        Assert.assertTrue(
                forgotPasswordPage.waitForUrlContains("message=success"),
                "URL seharusnya beralih ke URL dengan parameter 'message=success' dalam batas waktu wait!");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("message=success"),
                "URL parameter seharusnya mengandung 'message=success'! Actual: " + currentUrl);

        System.out.println("[FORGOT_PASSWORD] Step 7: Verify OTP field displayed");
        Assert.assertTrue(
                forgotPasswordPage.isOtpInputDisplayed(),
                "Field input OTP (id='otp') seharusnya tampil pada halaman reset password!");

        System.out.println("[FORGOT_PASSWORD] Result: PASS");
        System.out.println("[FORGOT_PASSWORD] ==========================================");
    }

    /**
     * Test 2 (Negative): Verifikasi pengajuan reset password gagal jika format email tidak valid.
     * 
     * Verifikasi:
     * - Client-side HTML5 Constraint Validation checkValidity() bernilai false
     * - HTML5 validity.typeMismatch bernilai true
     * - Submission diblokir di client-side
     */
    @Test(description = "Verifikasi pengajuan reset password gagal jika format email tidak valid")
    public void testForgotPasswordInvalidEmail() {

        String invalidEmail = "invalid-email-format";

        System.out.println("[FORGOT_PASSWORD] ==========================================");
        System.out.println("[FORGOT_PASSWORD] Test : Invalid Email Format");
        System.out.println("[FORGOT_PASSWORD] ------------------------------------------");
        System.out.println("[FORGOT_PASSWORD] Step 1: Open Login Page");
        forgotPasswordPage.openLoginPage();

        System.out.println("[FORGOT_PASSWORD] Step 2: Click 'Lupa password ?' link");
        forgotPasswordPage.clickForgotPasswordLink();

        System.out.println("[FORGOT_PASSWORD] Step 3: Input invalid email format");
        forgotPasswordPage.inputEmail(invalidEmail);

        System.out.println("[FORGOT_PASSWORD] Step 4: Click Submit button");
        forgotPasswordPage.clickSubmit();

        System.out.println("[FORGOT_PASSWORD] Step 5: Verify Native HTML5 Email Validation");
        Assert.assertFalse(
                forgotPasswordPage.isEmailInputValid(),
                "HTML5 checkValidity() seharusnya bernilai false untuk email invalid!");

        Assert.assertTrue(
                forgotPasswordPage.isEmailTypeMismatch(),
                "HTML5 validity.typeMismatch seharusnya bernilai true untuk email invalid!");

        System.out.println("[FORGOT_PASSWORD] Result: PASS");
        System.out.println("[FORGOT_PASSWORD] ==========================================");
    }
}
