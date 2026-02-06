-- Roles
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN')
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_MANAGER')
INSERT INTO roles (name) VALUES ('ROLE_MANAGER');

IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_SALES')
INSERT INTO roles (name) VALUES ('ROLE_SALES');

-- Products
IF NOT EXISTS (SELECT 1 FROM products WHERE sku = 'PRD-001')
INSERT INTO products (sku, name, price, stock_quantity, active, deleted, created_at)
VALUES ('PRD-001', 'Laptop', 1500.00, 10, 1, 0, GETDATE());

IF NOT EXISTS (SELECT 1 FROM products WHERE sku = 'PRD-002')
INSERT INTO products (sku, name, price, stock_quantity, active, deleted, created_at)
VALUES ('PRD-002', 'Mouse', 25.00, 50, 1, 0, GETDATE());

-- Customers
IF NOT EXISTS (SELECT 1 FROM customers WHERE email = 'ahmet@mail.com')
INSERT INTO customers (first_name, last_name, email, created_at, deleted)
VALUES ('Ahmet', 'Yilmaz', 'ahmet@mail.com', GETDATE(), 0);
