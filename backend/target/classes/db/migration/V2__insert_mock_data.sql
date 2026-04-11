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
INSERT INTO CustomOrders (UserID, ServiceType, ImageReferenceURL, ConfigurationJSON, Price)
VALUES
(2, 'AF_3D', 'https://s3/bucket/ref_gundam_custom.jpg', '{}', 1500000),
(2, 'Outfit', 'https://s3/bucket/user_logo2.png', '{"posX": 50, "posY": 100, "scaleX": 1.2}', 400000);

-- 5. Insert Simulasi Keranjang dan Checkout
INSERT INTO Orders (UserID, TotalAmount, DiscountID, FinalAmount, Status)
VALUES (2, 2500000, 2, 2200000, 'Paid');

-- Bapak Budi juga mengaitkannya ke OrderItems 
INSERT INTO OrderItems (OrderID, ProductID, Quantity, UnitPrice)
VALUES (1, 1, 1, 2500000);
