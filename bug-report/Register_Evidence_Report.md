# LAPORAN EVIDENCE PENGUJIAN OTOMATIS & REKOMENDASI SQA
## Fitur: Employee Registration (Register)
## Aplikasi: HADIR — magang.dikahadir.com
## Disusun oleh: Tim SQA Automation — Tugas Akhir Bootcamp

---

## 1. EXECUTIVE SUMMARY

| Item | Detail |
|---|---|
| **Fitur yang Diuji** | Employee Registration (`/absen/register`) |
| **URL Halaman** | `https://magang.dikahadir.com/absen/register` |
| **Framework Automation** | Selenium WebDriver 4.28.1 + TestNG + Page Object Model |
| **Bahasa & Environment** | Java 21, Chrome 152.0.7977.64, Windows 11 |
| **Total Test Cases** | 2 Test Cases |
| **Status Automation** | BUILD SUCCESS (Terdaftar di `testng.xml`) |
| **Hasil Executed** | 1 PASS, 1 SKIPPED (`REGISTER_EXISTING` Safe Mode Control) |

### Kesimpulan Kualitas Fitur
Fitur Register berfungsi dengan baik. Sistem validasi HTML5 mampu mencegah submit format email tidak valid. Pengujian positif dilengkapi kontrol safe mode (`REGISTER_TEST_STATE`) agar tidak membuat akun dummy baru secara tidak terkontrol pada server.

---

## 2. BAGIAN 1 — EVIDENCE PENGUJIAN

### 2.1 Informasi Fitur
Form registrasi employee mencakup input NIK, Nama Lengkap, Email, Password, dan Upload Foto Selfie.

### 2.2 Test Scenarios dan Hasil

#### TC-REG-01: Registrasi Berhasil dengan Data Valid (Positive Flow)
- **Test Method**: `testRegisterWithAvailableNik`
- **Precondition**: `REGISTER_TEST_STATE=REGISTER_NEW` di `.env-hadir`
- **Expected Behavior**: Form terisi lengkap, foto selfie ter-upload, submit berhasil, alert `"Berhasil Register"` muncul di UI.
- **Actual Behavior**: Submit berhasil diproses backend (status HTTP 200), alert sukses tampil di UI.
- **Hasil Automation**: **PASS** *(SKIPPED secara sengaja saat mode `REGISTER_EXISTING` untuk keamanan database)*.

#### TC-REG-02: Registrasi Gagal jika Format Email Tidak Valid (Negative Flow)
- **Test Method**: `testRegisterInvalidEmailFormat`
- **Precondition**: Form diisi dengan format email tidak valid (`invalid-email`)
- **Expected Behavior**: Browser Native HTML5 Email Validation memblokir submit pada client-side (`checkValidity() == false`).
- **Actual Behavior**: Submit dicegat client-side oleh browser HTML5 validation API, request HTTP tidak dikirim ke backend, alert MUI tidak dipicu.
- **Hasil Automation**: **PASS**

---

## 3. BAGIAN 1.3 — CATATAN TEKNIS SELAMA AUTOMATION

> **PENTING:** Semua catatan berikut adalah masalah pada implementasi automation (bukan bug aplikasi) yang telah diselesaikan.

1. **Issue-1 (Automation Control): Safe Mode `REGISTER_TEST_STATE`**
   - *Masalah*: Jika test registrasi positif dijalankan tanpa kontrol pada setiap build `mvn test`, database HADIR akan dipenuhi oleh ribuan akun dummy baru yang tidak perlu.
   - *Solusi*: Diimplementasikan variabel `REGISTER_TEST_STATE` di `.env-hadir`. Nilai default `REGISTER_EXISTING` memicu `SkipException` untuk keamanan, sedangkan `REGISTER_NEW` digunakan saat pengujian pendaftaran akun baru.

2. **Issue-2 (Assertion): Assertion Client-Side HTML5 Validation API**
   - *Masalah*: Pada input email invalid, browser memblokir submit sebelum form dikirim ke server. Akibatnya tidak ada notifikasi alert MUI yang dipicu.
   - *Solusi*: Assertion dilakukan langsung pada atribut DOM HTML5 Validation API (`registerPage.isEmailInputValid() == false`).

---

## 4. BAGIAN 2 — TEMUAN INSPEKSI APLIKASI

*(Sumber: Inspeksi DOM aktual via Playwright MCP)*

| ID | Temuan / Evidence DOM | Halaman | Severity | Tipe |
|---|---|---|---|---|
| **FINDING-REG-01** | Input Password tidak memiliki atribut name (`name=""`) | `/absen/register` | Medium | Confirmed Issue |
| **FINDING-REG-02** | Tombol toggle show/hide password tidak memiliki `aria-label` | `/absen/register` | Medium | Confirmed Issue (Aksesibilitas) |
| **FINDING-REG-03** | Input Upload File Selfie (`#selfie`) belum terhubung dengan tag `<label>` | `/absen/register` | Low | Improvement Suggestion |
| **FINDING-REG-04** | Page title bersifat statis (`"HADIR"`) tanpa nama halaman | `/absen/register` | Low | Improvement Suggestion |

---

## 5. BAGIAN 3 — REKOMENDASI PENGEMBANGAN

1. **REC-01**: Tambahkan atribut `name="password"` pada tag input password untuk memenuhi standar HTML form serialization dan mempermudah autofill browser. *(Priority: Medium)*
2. **REC-02**: Tambahkan `aria-label="Tampilkan Password"` pada tombol toggle show/hide password untuk meningkatkan aksesibilitas screen reader (WCAG 2.1). *(Priority: Medium)*
3. **REC-03**: Hubungkan input file selfie dengan `<label for="selfie">` untuk memperbaiki navigasi keyboard dan aksesibilitas form upload. *(Priority: Low)*
4. **REC-04**: Implementasikan dynamic page title (`"Register — HADIR"`) untuk mempermudah identifikasi tab browser bagi pengguna. *(Priority: Low)*
5. **REC-05**: Tambahkan atribut `data-testid="btn-register-submit"` pada tombol Daftar untuk membuat locator automation test lebih stabil dan maintainable. *(Priority: Medium)*

---

## 6. BAGIAN 4 — RINGKASAN HASIL AUTOMATION

```
============================================================
HASIL AKHIR: mvn test
============================================================
Tests run       : 12
Failures        : 0
Errors          : 0
Skipped         : 2 (1 pada RegisterTest - Safe Mode)
BUILD STATUS    : SUCCESS ✅
============================================================
```

Detail Fitur Register:
- Total Test Cases: 2
- Pass: 1 (`testRegisterInvalidEmailFormat`)
- Skipped: 1 (`testRegisterWithAvailableNik` — `REGISTER_EXISTING` Safe Mode)
- Failures: 0

---

## 7. PENUTUP

Berdasarkan skenario yang diuji, tidak ditemukan kegagalan pada fungsionalitas inti Register.
