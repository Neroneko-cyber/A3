# Mock Data & DDL Skema (SQL Server)

Berikut adalah basis kueri T-SQL untuk melakukan penciptaan tabel utama dan menyuntikkan Mock Data sementara agar sistem logika aplikasi memiliki landasan sebelum *live*.

## 1. DDL: Skema Relasional

```sql
-- Membangun Tabel Utama untuk Autentikasi Pengguna
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    PasswordHash NVARCHAR(255) NOT NULL,
    Role NVARCHAR(20) DEFAULT 'Customer',
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Membangun Tabel Katalog Inventaris (Bukan Custom)
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    Category NVARCHAR(50) NOT NULL, -- ActionFigure, Outfit, Manga, BluRay
    Name NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(18,2) NOT NULL,
    StockQuantity INT DEFAULT 0,
    ImageURL NVARCHAR(MAX)
);

-- Membangun Tabel Kupon Promosi / Diskon
CREATE TABLE Discounts (
    DiscountID INT IDENTITY(1,1) PRIMARY KEY,
    Code NVARCHAR(50) UNIQUE NOT NULL,
    DiscountType NVARCHAR(20) NOT NULL, -- Percentage (%), Fixed (Rp)
    DiscountValue DECIMAL(18,2) NOT NULL,
    ApplicableCategory NVARCHAR(50) -- All, ActionFigure, CustomOutfit, Custom3D
);

-- Membangun Tabel Pemesanan Berbasis Image / File Custom
CREATE TABLE CustomOrders (
    CustomOrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    ServiceType NVARCHAR(50), -- AF_3D (Action Figure), Outfit
    ImageReferenceURL NVARCHAR(MAX),
    ConfigurationJSON NVARCHAR(MAX), -- Menyimpan status canvas (X, Y, Rotasi, Skala)
    Price DECIMAL(18,2) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Membangun Tabel Penampung Transaksi
CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    TotalAmount DECIMAL(18,2) NOT NULL,
    DiscountID INT NULL FOREIGN KEY REFERENCES Discounts(DiscountID),
    FinalAmount DECIMAL(18,2) NOT NULL,
    Status NVARCHAR(50) DEFAULT 'Pending', -- Pending, Paid, Processing, Shipped
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Membangun Relasi Produk dan Detail Pesanan
CREATE TABLE OrderItems (
    OrderItemID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    ProductID INT NULL FOREIGN KEY REFERENCES Products(ProductID), 
    CustomOrderID INT NULL FOREIGN KEY REFERENCES CustomOrders(CustomOrderID), 
    Quantity INT NOT NULL DEFAULT 1,
    UnitPrice DECIMAL(18,2) NOT NULL
);
```

## 2. DML: Mock Data

```sql
-- 1. Insert Data Pengguna
INSERT INTO Users (Name, Email, PasswordHash, Role)
VALUES 
('Admin Otaku', 'admin@otaku.com', 'hashed_pass_admin', 'Admin'),
('Kolektor Budi', 'budi@gmail.com', 'hashed_pass_123', 'Customer');

-- 2. Insert Data Katalog Reguler (Products)
INSERT INTO Products (Category, Name, Description, Price, StockQuantity)
VALUES 
('ActionFigure', 'Saber 1/7 Scale Figure', 'High quality figure from Fate Series', 2500000, 10),
('Manga', 'Naruto Manga Vol 1', 'Manga volume 1', 55000, 100),
('BluRay', 'Your Name - Collector Edition', 'BluRay movie with extra scenes', 450000, 30),
('Outfit', 'Gojo Satoru Jacket (Mockup Base)', 'Base jacket for custom printing', 350000, 50);

-- 3. Insert Data Sistem Promosi / Diskon (Discounts)
INSERT INTO Discounts (Code, DiscountType, DiscountValue, ApplicableCategory)
VALUES 
('OTAKUNEW', 'Percentage', 10.00, 'All'),
('DISC300K', 'Fixed', 300000, 'All'),
('OUTFITFEST', 'Percentage', 15.00, 'CustomOutfit'),
('CUSTOM3DAF', 'Percentage', 20.00, 'Custom3D');

-- 4. Insert Simulasi Pesanan Custom (Draft Sebelum Pembayaran)
-- Menyertakan referensi design baju. Kolom JSON menandakan posisi x,y design pada mockup
INSERT INTO CustomOrders (UserID, ServiceType, ImageReferenceURL, ConfigurationJSON, Price)
VALUES
(2, 'AF_3D', 'https://s3/bucket/ref_gundam_custom.jpg', '{}', 1500000),
(2, 'Outfit', 'https://s3/bucket/user_logo2.png', '{"posX": 50, "posY": 100, "scaleX": 1.2}', 400000);

-- 5. Insert Simulasi Keranjang dan Checkout
-- Bapak Budi men-checkout produk reguler seharga 2.5jt, dan menggunakan kode diskon ID=2 (Fixed Rp300rb)
INSERT INTO Orders (UserID, TotalAmount, DiscountID, FinalAmount, Status)
VALUES (2, 2500000, 2, 2200000, 'Paid');

-- Bapak Budi juga mengaitkannya ke OrderItems 
INSERT INTO OrderItems (OrderID, ProductID, Quantity, UnitPrice)
VALUES (1, 1, 1, 2500000);
```
