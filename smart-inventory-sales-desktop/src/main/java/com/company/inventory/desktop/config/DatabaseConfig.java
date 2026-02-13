package com.company.inventory.desktop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:sqlite:inventory_local.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeSchema() {
        String createProductsSql = "CREATE TABLE IF NOT EXISTS local_products (" +
                                   "id INTEGER PRIMARY KEY, " +
                                   "name TEXT, " +
                                   "sku TEXT UNIQUE, " +
                                   "price REAL, " +
                                   "stock_quantity INTEGER, " +
                                   "sync_status TEXT, " +
                                   "last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

        String createCustomersSql = "CREATE TABLE IF NOT EXISTS local_customers (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "name TEXT, " +
                                    "email TEXT, " +
                                    "phone TEXT, " +
                                    "sync_status TEXT, " +
                                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createProductsSql);
            stmt.execute(createCustomersSql);
            System.out.println("Yerel SQLite şeması (Ürünler ve Müşteriler) başarıyla oluşturuldu.");
        } catch (Exception e) {
            System.err.println("Veritabanı başlatma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
