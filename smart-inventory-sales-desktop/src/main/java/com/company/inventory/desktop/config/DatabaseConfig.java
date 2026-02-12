package com.company.inventory.desktop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConfig {
    // MSSQL adresini sildik, SQLite dosya yolunu ekledik
    private static final String DB_URL = "jdbc:sqlite:inventory_local.db";

    public static Connection getConnection() throws Exception {
        // SQLite için kullanıcı adı ve şifre gerekmez
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeSchema() {
        // SQLite uyumlu tablo oluşturma sorgusu
        String createTableSql = "CREATE TABLE IF NOT EXISTS local_products (" +
                                "id INTEGER PRIMARY KEY, " +
                                "name TEXT, " +
                                "sku TEXT UNIQUE, " +
                                "price REAL, " +
                                "stock_quantity INTEGER, " +
                                "sync_status TEXT, " +
                                "last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
            System.out.println("Yerel SQLite şeması başarıyla oluşturuldu.");
        } catch (Exception e) {
            System.err.println("Veritabanı başlatma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}