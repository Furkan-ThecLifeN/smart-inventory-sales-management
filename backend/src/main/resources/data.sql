-- Roles
INSERT INTO roles (name) SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');
INSERT INTO roles (name) SELECT 'ROLE_MANAGER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_MANAGER');
INSERT INTO roles (name) SELECT 'ROLE_SALES' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_SALES');

-- Örnek Ürünler
INSERT INTO products (sku, name, price, stock_quantity, active, deleted, created_at) 
VALUES ('PRD-001', 'Laptop', 1500.00, 10, 1, 0, GETDATE());
INSERT INTO products (sku, name, price, stock_quantity, active, deleted, created_at) 
VALUES ('PRD-002', 'Mouse', 25.00, 50, 1, 0, GETDATE());

-- Örnek Müşteri
INSERT INTO customers (first_name, last_name, email, created_at, deleted) 
VALUES ('Ahmet', 'Yilmaz', 'ahmet@mail.com', GETDATE(), 0);