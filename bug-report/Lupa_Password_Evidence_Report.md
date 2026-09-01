# LAPORAN EVIDENCE PENGUJIAN OTOMATIS & REKOMENDASI SQA
## Fitur: Employee Lupa Password
## Aplikasi: HADIR — magang.dikahadir.com
## Disusun oleh: Tim SQA Automation — Tugas Akhir Bootcamp

---

## 1. EXECUTIVE SUMMARY

| Item | Detail |
|---|---|
| **Fitur yang Diuji** | Employee Lupa Password (`/absen/reset-password-request`) |
| **URL Halaman** | `https://magang.dikahadir.com/absen/reset-password-request` |
| **Framework Automation** | Selenium WebDriver 4.28.1 + TestNG + Page Object Model |
| **Bahasa & Environment** | Java 21, Chrome 152.0.7977.64, Windows 11 |
| **Total Test Cases** | 2 Test Cases |
| **Status Automation** | BUILD SUCCESS (Terdaftar di `testng.xml`) |
| **Hasil Executed** | 2 PASS, 0 Failures, 0 Skipped |

### Kesimpulan Kualitas Fitur
Fitur Lupa Password berfungsi dengan sangat baik. Permintaan reset password dengan email registered memicu pesan sukses dan redirect ke halaman verifikasi OTP.

---

## 2. BAGIAN 1 — EVIDENCE PENGUJIAN

### 2.1 Informasi Fitur
Permintaan reset password melalui input email registered.

### 2.2 Test Scenarios dan Hasil

#### TC-FGT-01: Reset Password Valid (Positive Flow)
- **Test Method**: `testForgotPasswordValidEmail`
- **Expected Behavior**: Toast sukses `"Link reset password terkirim"` muncul, redirect ke URL `message=success`, field OTP (`id="otp"`) tampil di layar.
- **Actual Behavior**: Toast sukses tampil, URL berpindah ke `message=success`, field OTP terverifikasi tampil di layar.
- **Hasil Automation**: **PASS**

#### TC-FGT-02: Format Email Invalid (Negative Flow)
- **Test Method**: `testForgotPasswordInvalidEmail`
- **Expected Behavior**: HTML5 validation memblokir submit client-side (`checkValidity() == false`, `typeMismatch == true`).
- **Actual Behavior**: Submit diblokir browser client-side, request HTTP tidak dikirim ke server.
- **Hasil Automation**: **PASS**

---

## 3. BAGIAN 1.3 — CATATAN TEKNIS SELAMA AUTOMATION

> **PENTING:** Semua catatan berikut adalah masalah pada implementasi automation (bukan bug aplikasi) yang telah diselesaikan.

1. **Issue-1 (Routing Race Condition): Asynchronous Router Redirect Substring Match**
   - *Masalah*: Awalnya test menggunakan `waitForUrlContains("/absen/reset-password")`. Wait condition ini langsung bernilai true instan tanpa menunggu redirect karena URL asal `/absen/reset-password-request` sudah mengandung substring tersebut.
   - *Solusi*: Fraction diubah menjadi `waitForUrlContains("message=success")` yang 100% unik untuk halaman target setelah redirect.

---

## 4. BAGIAN 2 — TEMUAN INSPEKSI APLIKASI

*(Sumber: Inspeksi DOM aktual via Playwright MCP)*

| ID | Temuan / Evidence DOM | Halaman | Severity | Tipe |
|---|---|---|---|---|
| **FINDING-FGT-01** | Input Email tidak memiliki atribut `required` pada HTML tag | `/absen/reset-password-request` | Medium | Confirmed Issue |
| **FINDING-FGT-02** | Tidak ada timer countdown resend OTP pada halaman verifikasi OTP | `/absen/reset-password` | Low | Improvement Suggestion |
| **FINDING-FGT-03** | Page title bersifat statis (`"HADIR"`) tanpa nama halaman | `/absen/reset-password-request` | Low | Improvement Suggestion |

---

## 5. BAGIAN 3 — REKOMENDASI PENGEMBANGAN

1. **REC-01**: Tambahkan atribut `required` pada tag input email reset password untuk memblokir submit kosong di client-side. *(Priority: Medium)*
2. **REC-02**: Tambahkan timer countdown (misal 60 detik) sebelum tombol Kirim Ulang OTP aktif untuk mencegah spam request OTP. *(Priority: Medium)*
3. **REC-03**: Implementasikan dynamic page title (`"Lupa Password — HADIR"`) untuk mempermudah pengguna. *(Priority: Low)*
4. **REC-04**: Tambahkan atribut `data-testid="btn-reset-submit"` pada tombol Submit untuk meningkatkan ketahanan automation test. *(Priority: Medium)*
5. **REC-05**: Berikan batasan maksimal percobaan input OTP (misal 3-5x salah) untuk mencegah serangan brute-force. *(Priority: High)*

---

## 6. BAGIAN 4 — RINGKASAN HASIL AUTOMATION

```
============================================================
HASIL AKHIR: mvn test
============================================================
Tests run       : 12
Failures        : 0
Errors          : 0
Skipped         : 2
BUILD STATUS    : SUCCESS ✅
============================================================
```

Detail Fitur Lupa Password:
- Total Test Cases: 2
- Pass: 2 (`testForgotPasswordValidEmail`, `testForgotPasswordInvalidEmail`)
- Failures: 0

---

## 7. PENUTUP

Berdasarkan skenario yang diuji, tidak ditemukan kegagalan pada fungsionalitas inti Lupa Password.
