-- V7: Reset password akun test ke format BCrypt murni (tanpa kombinasi name+email)
-- Password baru: Admin123! untuk admin, Budi123! untuk customer
-- Hash dibuat dengan BCrypt strength=12

UPDATE Users SET PasswordHash = '$2a$12$2MmPQXrP59Q.J5LWrJDKxuQEHUwvDO/SInb1JCf6g4YLYAbkKbLne'
WHERE Email = 'admin@otaku.com';
-- Hash di atas = BCrypt(Admin123!)

UPDATE Users SET PasswordHash = '$2a$12$7Y2yODZcrgABcSJ8AMsDAeVB15HNdRQHUBbEAJVXSlhLvO69YDsaS'
WHERE Email = 'budi@gmail.com';
-- Hash di atas = BCrypt(Budi123!)
