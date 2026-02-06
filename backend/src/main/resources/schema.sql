-- Örnek: Roles Tablosu
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='roles' AND xtype='U')
CREATE TABLE roles (id INT IDENTITY(1,1) PRIMARY KEY, name VARCHAR(20) UNIQUE);