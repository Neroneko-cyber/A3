# Arsitektur Backend & Lingkungan Pengembangan: E-Commerce Otaku

Berdasarkan keputusan Anda untuk menggunakan teknologi server yang memiliki tingkat keamanan tinggi (minim *vulnerabilities*) serta berfokus pada kemudahan pemeliharaan (*maintenance*), rancangan sistem Backend ini akan menggunakan basis **Spring Boot**. Dokumentasi ini juga menspesifikasikan *Integrated Development Environment (IDE)* secara mutlak.

## 1. Tumpukan Teknologi (Tech Stack) Ideal
- **Framework Utama:** **Spring Boot 3.2.x** dengan **Java 21**. (Keputusan final: ini adalah spesifikasi bahasa termutakhir dengan keamanan paling kokoh dan pembaruan sekuriti tangguh).
- **Akses Database:** **Spring Data JPA / Hibernate** untuk menghubungkannya secara aman dan mulus ke instans SQL Server.
- **Migrasi Otomatis:** Menggunakan **Flyway** atau **Liquibase**. Sistem akan me-*maintain* pembaruan pemetaan database secara otomatis ketika aplikasi di-*run*.
- **Keamanan Utama:** **Spring Security** yang dilengkapi perlindungan CSRF (CORS) ketat dan **JWT (JSON Web Token)** untuk Autentikasi Pengguna.

## 2. Lingkungan Pengembangan (IDE) & Konfigurasi Anti-Bug
Pengembangan (*coding*) dan perakitan *package* backend ini dikonstruksi agar *seamless* dan terintegrasi mulus 100% tanpa komplikasi celah *"bug"* ke dalam editor milik Anda:
- **Editor Kode / IDE:** `IntelliJ IDEA Community Edition 2025.2.6.1`
- **Strategi Integrasi Lancar:** 
  - Mengonfigurasi *Maven wrapper* bawaan agar pengindeksan `IntelliJ IDEA` dapat memuat *libraries* Spring tanpa ada depedensi yang macet (Failed to Load/Unresolved symbols).
  - Melakukan instalasi Project SDK dan *Language Module Level* khusus pada kompilator bawaan *IntelliJ* yang sesuai dan diarahkan persis pada *Java Development Kit 21*.
  - Melindungi anotasi prosesor bawaan agar tidak tertabrak oleh fitur IDE Community.

## 3. Sistem Maintenance Semi-Otomatis (Self-healing & Monitoring)
Untuk mendapatkan sistem yang mandiri dan tidak selalu merepotkan administrator:
1. **Pemantauan (Spring Boot Actuator):** Mengaktifkan *endpoints* kesehatan (`/actuator/health`). Mendeteksi jika koneksi SQL Server mati atau RAM penuh.
2. **Pembersihan Database Berkala (Cron Jobs Scheduler):**
   - Akan ditanamkan fungsi `@Scheduled` otomatis untuk melakukan "sapu bersih". 
   - *Auto-delete* pesanan fiktif/pending lebih dari 3 hari, dan *auto-expired* kode Diskon lama.
3. **Manajemen Log Terpusat:** Konfigurasi `Logback` untuk rotasi otomatis *file logging* berdasarkan per hari.

## 4. Mitigasi Kerentanan & Celah (Vulnerability Protection)
- **Dependency Analytics:** Integrasi blokade *OWASP Check* untuk menghentikan peringatan CVE fatal.
- **SQL Injection Prevention:** Penggunaan instansi *Prepared Statements* otomatis Hibernate JPA.
- **Pengkodean Password:** Menggunakan sistem *cost standard hashed* `BCryptPasswordEncoder`.

## 5. Sinkronisasi Modul dengan Frontend
- File gambar *Custom 3D Action Figure* akan di-*intercept* oleh **Spring Web Multipart**, kemudian URL resminya ditautkan ke tabel `CustomOrders`.
- Titik koordinat kanvas *Outfit* disimpan sebagai format Map JSON dan dimasukkan ke `ConfigurationJSON` pada SQL Server.
