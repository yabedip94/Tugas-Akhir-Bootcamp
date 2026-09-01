# LAPORAN EVIDENCE PENGUJIAN OTOMATIS & REKOMENDASI SQA
## Fitur: Employee Login
## Aplikasi: HADIR — magang.dikahadir.com
## Disusun oleh: Tim SQA Automation — Tugas Akhir Bootcamp

---

## 1. EXECUTIVE SUMMARY

| Item | Detail |
|---|---|
| **Fitur yang Diuji** | Employee Login (`/absen/login`) |
| **URL Halaman** | `https://magang.dikahadir.com/absen/login` |
| **Framework Automation** | Selenium WebDriver 4.28.1 + TestNG + Page Object Model |
| **Bahasa & Environment** | Java 21, Chrome 152.0.7977.64, Windows 11 |
| **Total Test Cases** | 2 Test Cases |
| **Status Automation** | BUILD SUCCESS (Terdaftar di `testng.xml`) |
| **Hasil Executed** | 2 PASS, 0 Failures, 0 Skipped |

### Kesimpulan Kualitas Fitur
Fitur Login berfungsi dengan sangat stabil pada skenario positif maupun negatif. Otentikasi kredensial valid dan penolakan akun tidak terdaftar beroperasi sesuai spesifikasi.

---

## 2. BAGIAN 1 — EVIDENCE PENGUJIAN

### 2.1 Informasi Fitur
Otentikasi employee menggunakan email dan password.

### 2.2 Test Scenarios dan Hasil

#### TC-LOG-01: Login Kredensial Valid (Positive Flow)
- **Test Method**: `testLoginBerhasilDenganKredensialValid`
- **Expected Behavior**: Login berhasil, redirect ke `/apps/absent`, nama user & dashboard tampil di layar.
- **Actual Behavior**: Redirect berhasil, URL `/apps/absent`, elemen welcome text terverifikasi di UI.
- **Hasil Automation**: **PASS**

#### TC-LOG-02: Login Kredensial Invalid (Negative Flow)
- **Test Method**: `testLoginGagalDenganKredensialTidakValid`
- **Expected Behavior**: Login ditolak, pesan error `"Akun tidak ditemukan"` muncul di layar.
- **Actual Behavior**: Pesan error `"Akun tidak ditemukan"` terdeteksi di UI.
- **Hasil Automation**: **PASS**

---

## 3. BAGIAN 1.3 — CATATAN TEKNIS SELAMA AUTOMATION

> **PENTING:** Semua catatan berikut adalah masalah pada implementasi automation (bukan bug aplikasi) yang telah diselesaikan.

1. **Issue-1 (Synchronization): Asynchronous Next.js Client-Side Navigation Delay**
   - *Masalah*: Pada awal pembuatan test, Selenium mengecek `driver.getCurrentUrl()` terlalu cepat sebelum router Next.js selesai berpindah dari `/absen/login` ke `/apps/absent`.
   - *Solusi*: Ditambahkan explicit wait `waitForVisible(monthButton)` / welcome text sebelum assertion URL dilakukan.

---

## 4. BAGIAN 2 — TEMUAN INSPEKSI APLIKASI

*(Sumber: Inspeksi DOM aktual via Playwright MCP)*

| ID | Temuan / Evidence DOM | Halaman | Severity | Tipe |
|---|---|---|---|---|
| **FINDING-LOG-01** | Tombol toggle show/hide password tidak memiliki `aria-label` | `/absen/login` | Medium | Confirmed Issue (Aksesibilitas) |
| **FINDING-LOG-02** | Link `"Lupa password ?"` menggunakan click handler tanpa atribut `href` | `/absen/login` | Low | Confirmed Issue (Aksesibilitas) |
| **FINDING-LOG-03** | Page title bersifat statis (`"HADIR"`) tanpa nama halaman | `/absen/login` | Low | Improvement Suggestion |

---

## 5. BAGIAN 3 — REKOMENDASI PENGEMBANGAN

1. **REC-01**: Tambahkan `aria-label="Tampilkan Password"` pada tombol toggle show/hide password untuk meningkatkan aksesibilitas screen reader. *(Priority: Medium)*
2. **REC-02**: Berikan atribut `href="/absen/reset-password-request"` pada link Lupa Password untuk memungkinkan navigasi keyboard dan open-in-new-tab. *(Priority: Low)*
3. **REC-03**: Implementasikan dynamic page title (`"Login — HADIR"`) untuk mempermudah pengguna saat membuka banyak tab. *(Priority: Low)*
4. **REC-04**: Tambahkan atribut `data-testid="btn-login-submit"` pada tombol Masuk untuk meningkatkan ketahanan locator automation test. *(Priority: Medium)*
5. **REC-05**: Tampilkan loading spinner pada tombol Masuk saat submit sedang berlangsung untuk mencegah double-click pengguna. *(Priority: Medium)*

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

Detail Fitur Login:
- Total Test Cases: 2
- Pass: 2 (`testLoginBerhasilDenganKredensialValid`, `testLoginGagalDenganKredensialTidakValid`)
- Failures: 0

---

## 7. PENUTUP

Berdasarkan skenario yang diuji, tidak ditemukan kegagalan pada fungsionalitas inti Login.
