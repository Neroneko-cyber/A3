# Arsitektur Database & Diagram Sistem (SQL Server)

Dokumen ini berisi pemodelan data dan alur sistem untuk aplikasi web E-commerce Otaku Store, mencakup fitur reguler dan modul kustomisasi (3D AF & Outfit).

## 1. Entity Relationship Diagram (ERD)
ERD ini memetakan relasi antar entitas utama di dalam database pangkalan data SQL Server. Skema melibatkan pemecahan tipe pemesanan antara barang reguler (`Products`) dan layanan berbasis lampiran (*file image*) yang di-submit user (`CustomOrders`). Kupon (`Discounts`) terhubung ke tabel `Orders` untuk merangkum total pesanan.

```mermaid
erDiagram
    USERS ||--o{ ORDERS : "melakukan"
    USERS ||--o{ CUSTOM_ORDERS : "membuat"
    ORDERS ||--|{ ORDER_ITEMS : "memiliki"
    PRODUCTS ||--o{ ORDER_ITEMS : "termasuk"
    CUSTOM_ORDERS ||--o{ ORDER_ITEMS : "terkait (opsional)"
    DISCOUNTS ||--o{ ORDERS : "diaplikasikan"

    USERS {
        int UserID PK
        string Name
        string Email
        string PasswordHash
        string Role "Admin / Customer"
    }
    PRODUCTS {
        int ProductID PK
        string Category "ActionFigure, Outfit, Manga, BluRay"
        string Name
        decimal Price
        int Stock
    }
    ORDERS {
        int OrderID PK
        int UserID FK
        decimal TotalAmount
        int DiscountID FK
        decimal FinalAmount
        string Status
    }
    ORDER_ITEMS {
        int OrderItemID PK
        int OrderID FK
        int ProductID FK
        int CustomOrderID FK "nullable"
        int Quantity
        decimal UnitPrice
    }
    CUSTOM_ORDERS {
        int CustomOrderID PK
        int UserID FK
        string Type "AF_3D / Outfit"
        string ImageRefURL
        string ConfigJSON
        decimal Price
    }
    DISCOUNTS {
        int DiscountID PK
        string Code
        string Type "Percentage / Fixed"
        decimal Value
        string ApplicableCategory
    }
```

## 2. Data Flow Diagram (DFD) - Level 0 (Context Diagram)

DFD yang menunjukkan pertukaran informasi utama di sekitar aplikasi.

```mermaid
graph TD
    User([Pelanggan]) -->|1. Mencari Produk / Form Custom| Sys((Sistem E-Commerce Otaku))
    User -->|2. Unggah Gambar & Rotasi Desain| Sys
    User -->|3. Input Kode Diskon| Sys
    User -->|4. Input Pembayaran| Sys
    
    Sys -->|A. Tampilkan Info Produk & Area Mockup| User
    Sys -->|B. Hitung Total & Diskon Akhir| User
    Sys -->|C. Status Pesanan| User
    
    Sys <-->|Membaca & Menyimpan State| DB[(SQL Server Database)]
    
    Admin([Admin Toko]) -->|Kelola Inventaris, Kupon & Lihat Pesanan 3D| Sys
    Sys -->|Laporan Konversi & Unduh File Cetak 3D| Admin
```

## 3. Flowchart Pembelian (User Journey menuju Kalkulasi Checkout)

Flowchart sistem E-commerce dari saat pelanggan login hingga checkout beserta fungsionalitas cabang diskon (Reguler VS Servis Custom).

```mermaid
graph TD
    A[Mulai] --> B(Login / Registrasi)
    B --> C{Pilih Aktivitas}
    
    C -->|Beli Barang Reguler| D[Pilih dari Katalog Manga/DVD/AF]
    C -->|Pesan Custom 3D AF| E[Unggah Gambar Referensi 2D / Cetak Biru]
    C -->|Pesan Custom Outfit| F[Unggah Image & Edit Posisi di Canvas Mockup]
    
    D --> G[Masukkan ke Keranjang]
    E --> G
    F --> G
    
    G --> H{Cek Kondisi Diskon}
    H -->|Input Kode Promo| I{Tipe Diskon?}
    H -->|Tidak Ada Diskon| K[Lanjut Checkout Tanpa Potongan]
    
    I -->|Persentase| J[Hitung % Potongan dari Harga]
    I -->|Fix Rupiah| J2[Kurangi Nominal Tunai Langsung]
    
    J --> L[Dapatkan Harga Final]
    J2 --> L
    L --> M[Lanjut Checkout]
    K --> M
    
    M --> N[Sistem Pembayaran / Payment Gateway]
    N --> O[Menyimpan Rekam Jejak ke Database SQL Server]
    O --> P[Selesai]
```
