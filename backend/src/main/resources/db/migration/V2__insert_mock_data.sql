-- 1. Insert Data Pengguna (Hanya jika email belum ada)
IF NOT EXISTS (SELECT 1 FROM Users WHERE Email = 'admin@otaku.com')
    INSERT INTO Users (Name, Email, PasswordHash, Role) VALUES ('Admin Otaku', 'admin@otaku.com', '$2a$10$z/40BcNRMNEldOFfdtUdX.4IgCJ4onylQlM5GAi44TIa1KA0tm112', 'Admin');

IF NOT EXISTS (SELECT 1 FROM Users WHERE Email = 'budi@gmail.com')
    INSERT INTO Users (Name, Email, PasswordHash, Role) VALUES ('Kolektor Budi', 'budi@gmail.com', '$2a$10$JYvoKZ7SgnxF35Tf80dFLu.oBGFOaWUTsGimQJvkoF0dbseukZOpa', 'Customer');

-- 2. Insert Data Katalog Reguler (Products) (Hanya jika nama produk belum ada)
IF NOT EXISTS (SELECT 1 FROM Products WHERE Name = 'Saber 1/7 Scale Figure')
    INSERT INTO Products (Category, Name, Description, Price, StockQuantity) VALUES ('ActionFigure', 'Saber 1/7 Scale Figure', 'High quality figure from Fate Series', 2500000, 10);

IF NOT EXISTS (SELECT 1 FROM Products WHERE Name = 'Naruto Manga Vol 1')
    INSERT INTO Products (Category, Name, Description, Price, StockQuantity) VALUES ('Manga', 'Naruto Manga Vol 1', 'Manga volume 1', 55000, 100);

IF NOT EXISTS (SELECT 1 FROM Products WHERE Name = 'Your Name - Collector Edition')
    INSERT INTO Products (Category, Name, Description, Price, StockQuantity) VALUES ('BluRay', 'Your Name - Collector Edition', 'BluRay movie with extra scenes', 450000, 30);

IF NOT EXISTS (SELECT 1 FROM Products WHERE Name = 'Gojo Satoru Jacket (Mockup Base)')
    INSERT INTO Products (Category, Name, Description, Price, StockQuantity) VALUES ('Outfit', 'Gojo Satoru Jacket (Mockup Base)', 'Base jacket for custom printing', 350000, 50);

-- 3. Insert Data Sistem Promosi / Diskon (Discounts) (Hanya jika kode belum ada)
IF NOT EXISTS (SELECT 1 FROM Discounts WHERE Code = 'OTAKUNEW')
    INSERT INTO Discounts (Code, DiscountType, DiscountValue, ApplicableCategory) VALUES ('OTAKUNEW', 'Percentage', 10.00, 'All');

IF NOT EXISTS (SELECT 1 FROM Discounts WHERE Code = 'DISC300K')
    INSERT INTO Discounts (Code, DiscountType, DiscountValue, ApplicableCategory) VALUES ('DISC300K', 'Fixed', 300000, 'All');

IF NOT EXISTS (SELECT 1 FROM Discounts WHERE Code = 'OUTFITFEST')
    INSERT INTO Discounts (Code, DiscountType, DiscountValue, ApplicableCategory) VALUES ('OUTFITFEST', 'Percentage', 15.00, 'CustomOutfit');

IF NOT EXISTS (SELECT 1 FROM Discounts WHERE Code = 'CUSTOM3DAF')
    INSERT INTO Discounts (Code, DiscountType, DiscountValue, ApplicableCategory) VALUES ('CUSTOM3DAF', 'Percentage', 20.00, 'Custom3D');

-- 4. Insert Simulasi Pesanan Custom (Hanya jika belum ada simulasi untuk user 2)
IF NOT EXISTS (SELECT 1 FROM CustomOrders WHERE UserID = 2 AND ServiceType = 'AF_3D')
    INSERT INTO CustomOrders (UserID, ServiceType, ImageReferenceURL, ConfigurationJSON, Price) VALUES (2, 'AF_3D', 'https://s3/bucket/ref_gundam_custom.jpg', '{}', 1500000);

IF NOT EXISTS (SELECT 1 FROM CustomOrders WHERE UserID = 2 AND ServiceType = 'Outfit')
    INSERT INTO CustomOrders (UserID, ServiceType, ImageReferenceURL, ConfigurationJSON, Price) VALUES (2, 'Outfit', 'https://s3/bucket/user_logo2.png', '{"posX": 50, "posY": 100, "scaleX": 1.2}', 400000);

-- 5. Insert Simulasi Keranjang dan Checkout (Hanya jika belum ada order untuk user 2)
IF NOT EXISTS (SELECT 1 FROM Orders WHERE UserID = 2 AND TotalAmount = 2500000)
BEGIN
    INSERT INTO Orders (UserID, TotalAmount, DiscountID, FinalAmount, Status) VALUES (2, 2500000, 2, 2200000, 'Paid');
    INSERT INTO OrderItems (OrderID, ProductID, Quantity, UnitPrice) VALUES (SCOPE_IDENTITY(), 1, 1, 2500000);
END;
