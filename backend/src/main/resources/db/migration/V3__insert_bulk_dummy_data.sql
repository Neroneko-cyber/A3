-- ============================================================
-- V3: Bulk Dummy Data — 500 Users + 500 Products (Total 1000)
-- ============================================================

-- ============ USERS (500 data) ============
-- Menggunakan loop WHILE karena SQL Server mendukung procedural DML

DECLARE @i INT = 1;
WHILE @i <= 500
BEGIN
    INSERT INTO Users (Name, Email, PasswordHash, Role)
    VALUES (
        CONCAT('User_', @i),
        CONCAT('user', @i, '@otakustore.com'),
        CONCAT('hashed_pwd_', @i),
        CASE 
            WHEN @i % 50 = 0 THEN 'Admin'
            ELSE 'Customer'
        END
    );
    SET @i = @i + 1;
END;

-- ============ PRODUCTS (500 data) ============

DECLARE @j INT = 1;
WHILE @j <= 500
BEGIN
    DECLARE @cat NVARCHAR(50);
    DECLARE @prefix NVARCHAR(100);
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

    SET @basePrice = CASE @cat
        WHEN 'ActionFigure' THEN 2000000 + (@j * 1500)
        WHEN 'Manga'        THEN 45000   + (@j * 200)
        WHEN 'BluRay'       THEN 350000  + (@j * 800)
        WHEN 'Outfit'       THEN 250000  + (@j * 600)
    END;

    INSERT INTO Products (Category, Name, Description, Price, StockQuantity)
    VALUES (
        @cat,
        CONCAT(@prefix, ' #', @j),
        CONCAT('Premium authentic Japanese merchandise — ', @cat, ' item number ', @j, '. Limited stock, imported directly.'),
        @basePrice,
        ABS(CHECKSUM(NEWID())) % 200 + 1
    );

    SET @j = @j + 1;
END;
