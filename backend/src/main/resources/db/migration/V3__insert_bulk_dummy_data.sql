-- ============================================================
-- V3: Bulk Dummy Data — 500 Users + 500 Products (Total 1000)
-- ============================================================

-- ============ USERS (500 data) ============
-- Menggunakan loop WHILE karena SQL Server mendukung procedural DML

DECLARE @i INT = 1;
DECLARE @email NVARCHAR(100);
WHILE @i <= 500
BEGIN
    SET @email = CONCAT('user', @i, '@otakustore.com');
    IF NOT EXISTS (SELECT 1 FROM Users WHERE Email = @email)
    BEGIN
        INSERT INTO Users (Name, Email, PasswordHash, Role)
        VALUES (
            CONCAT('User_', @i),
            @email,
            CONCAT('hashed_pwd_', @i),
            CASE 
                WHEN @i % 50 = 0 THEN 'Admin'
                ELSE 'Customer'
            END
        );
    END
    SET @i = @i + 1;
END;

-- ============ PRODUCTS (500 data) ============

DECLARE @j INT = 1;
WHILE @j <= 500
BEGIN
    DECLARE @cat NVARCHAR(50);
    DECLARE @prefix NVARCHAR(100);
    DECLARE @prodName NVARCHAR(255);
    DECLARE @basePrice DECIMAL(18,2);

    -- Rotasi 4 kategori
    SET @cat = CASE @j % 4
        WHEN 0 THEN 'ActionFigure'
        WHEN 1 THEN 'Manga'
        WHEN 2 THEN 'BluRay'
        WHEN 3 THEN 'Outfit'
    END;

    SET @prefix = CASE @cat
        WHEN 'ActionFigure' THEN 'Scale Figure Edition'
        WHEN 'Manga' THEN 'Manga Volume'
        WHEN 'BluRay' THEN 'BluRay Collector'
        WHEN 'Outfit' THEN 'Streetwear Apparel'
    END;

    SET @prodName = CONCAT(@prefix, ' #', @j);

    SET @basePrice = CASE @cat
        WHEN 'ActionFigure' THEN 2000000 + (@j * 1500)
        WHEN 'Manga'        THEN 45000   + (@j * 200)
        WHEN 'BluRay'       THEN 350000  + (@j * 800)
        WHEN 'Outfit'       THEN 250000  + (@j * 600)
    END;

    IF NOT EXISTS (SELECT 1 FROM Products WHERE Name = @prodName)
    BEGIN
        INSERT INTO Products (Category, Name, Description, Price, StockQuantity)
        VALUES (
            @cat,
            @prodName,
            CONCAT('Premium authentic Japanese merchandise — ', @cat, ' item number ', @j, '. Limited stock, imported directly.'),
            @basePrice,
            ABS(CHECKSUM(NEWID())) % 200 + 1
        );
    END

    SET @j = @j + 1;
END;
