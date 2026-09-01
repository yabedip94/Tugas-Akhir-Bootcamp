# LAPORAN EVIDENCE PENGUJIAN OTOMATIS & REKOMENDASI SQA
## Fitur: Employee Koreksi Absen
## Aplikasi: HADIR — magang.dikahadir.com
## Disusun oleh: Tim SQA Automation — Tugas Akhir Bootcamp

---

## 1. EXECUTIVE SUMMARY

| Item | Detail |
|---|---|
| **Fitur yang Diuji** | Employee Koreksi Absen (`/apps/absent/correction`) |
| **URL Halaman** | `https://magang.dikahadir.com/apps/absent/correction` |
| **Framework Automation** | Selenium WebDriver 4.28.1 + TestNG + Page Object Model |
| **Bahasa & Environment** | Java 21, Chrome 152.0.7977.64, Windows 11 |
| **Total Test Cases** | 2 Test Cases |
| **Status Automation** | BUILD SUCCESS (Terdaftar di `testng.xml`) |
| **Hasil Executed** | 2 PASS, 0 Failures, 0 Skipped |

### Kesimpulan Kualitas Fitur
Fitur Koreksi Absen beroperasi dengan sangat baik. Formulir pengajuan koreksi dengan picker waktu dan tipe absen dapat disubmit dan diproses oleh server.

---

## 2. BAGIAN 1 — EVIDENCE PENGUJIAN

### 2.1 Informasi Fitur
Pengajuan koreksi jam masuk, jam keluar, dan tipe absen (WFO/WFH).

### 2.2 Test Scenarios dan Hasil

#### TC-COR-01: Ajukan Koreksi Valid (Positive Flow)
- **Test Method**: `testAjukanKoreksiAbsenBerhasil`
- **Expected Behavior**: Modal terbuka, jam dipilih, tipe WFH dipilih, submit berhasil, modal tertutup, record tersimpan di list.
- **Actual Behavior**: Form terisi, submit berhasil, modal tertutup, record baru muncul di list koreksi.
- **Hasil Automation**: **PASS**

#### TC-COR-02: Ajukan Koreksi Field Kosong (Negative Flow)
- **Test Method**: `testAjukanKoreksiAbsenGagalFieldKosong`
- **Expected Behavior**: Form ditolak, pesan error `"Salah satu harus diisi!"` muncul, modal tetap terbuka.
- **Actual Behavior**: Pesan error `"Salah satu harus diisi!"` tampil, modal tidak tertutup.
- **Hasil Automation**: **PASS**

---

## 3. BAGIAN 1.3 — CATATAN TEKNIS SELAMA AUTOMATION

> **PENTING:** Semua catatan berikut adalah masalah pada implementasi automation (bukan bug aplikasi) yang telah diselesaikan.

1. **Issue-1 (Interaksi Component): Material-UI TimePicker Popup Handling**
   - *Masalah*: Memilih waktu pada MUI TimePicker memerlukan interaksi popup berlayar yang sensitif terhadap event dispatching.
   - *Solusi*: Method `selectJamMasukFromPicker()` dan `selectJamKeluarFromPicker()` menggunakan kombinasi JavascriptExecutor dan DOM event triggering agar pemilihan jam stabil.

---

## 4. BAGIAN 2 — TEMUAN INSPEKSI APLIKASI

*(Sumber: Inspeksi DOM aktual via Playwright MCP)*

| ID | Temuan / Evidence DOM | Halaman | Severity | Tipe |
|---|---|---|---|---|
| **FINDING-COR-01** | Pesan error validasi `"Salah satu harus diisi!"` kurang spesifik | `/apps/absent/correction` | Medium | Confirmed Issue (UX) |
| **FINDING-COR-02** | Tidak ada toast notification sukses yang muncul setelah submit formulir | `/apps/absent/correction` | Medium | Improvement Suggestion |
| **FINDING-COR-03** | Page title bersifat statis (`"HADIR"`) tanpa nama halaman | `/apps/absent/correction` | Low | Improvement Suggestion |

---

## 5. BAGIAN 3 — REKOMENDASI PENGEMBANGAN

1. **REC-01**: Perbaiki pesan validasi menjadi `"Jam Masuk dan Jam Keluar wajib diisi"` agar user mendapat petunjuk yang jelas. *(Priority: Medium)*
2. **REC-02**: Tampilkan MUI Snackbar dengan konfirmasi `"Pengajuan koreksi absen berhasil dikirim"` setelah submit. *(Priority: Medium)*
3. **REC-03**: Implementasikan dynamic page title (`"Koreksi Absen — HADIR"`) untuk mempermudah pengguna membedakan tab. *(Priority: Low)*
4. **REC-04**: Tambahkan atribut `data-testid="btn-ajukan-koreksi"` pada tombol dan form input untuk meningkatkan ketahanan automation test. *(Priority: Medium)*
5. **REC-05**: Tambahkan badge status (Pending/Approved/Rejected) pada kartu list koreksi untuk memudahkan employee memantau persetujuan. *(Priority: Medium)*

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

Detail Fitur Koreksi Absen:
- Total Test Cases: 2
- Pass: 2 (`testAjukanKoreksiAbsenBerhasil`, `testAjukanKoreksiAbsenGagalFieldKosong`)
- Failures: 0

---

## 7. PENUTUP

Berdasarkan skenario yang diuji, tidak ditemukan kegagalan pada fungsionalitas inti Koreksi Absen.
