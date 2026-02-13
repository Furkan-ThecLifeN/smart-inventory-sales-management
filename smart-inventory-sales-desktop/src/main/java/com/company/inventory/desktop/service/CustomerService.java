package com.company.inventory.desktop.service;

import com.company.inventory.desktop.config.DatabaseConfig;
import com.company.inventory.desktop.model.LocalCustomerDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CustomerService {

    public void saveCustomer(LocalCustomerDto customer) {
        customer.setCreatedAt(LocalDateTime.now());
        saveToLocalDb(customer, "PENDING");
    }

    private void saveToLocalDb(LocalCustomerDto customer, String status) {
        String sql = "INSERT INTO local_customers (name, email, phone, sync_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, status);
            
            pstmt.executeUpdate();
            System.out.println("✅ SQLite Başarılı: " + customer.getName() + " veritabanına eklendi [" + status + "]");
            
        } catch (Exception e) {
            System.err.println("❌ SQLite Kayıt Hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPendingCustomers() {
        String selectSql = "SELECT * FROM local_customers WHERE sync_status = 'PENDING'";
        String updateSql = "UPDATE local_customers SET sync_status = 'SYNCED' WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("🔄 " + name + " backend'e gönderiliyor...");

                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    System.out.println("✅ " + name + " durumu SYNCED olarak güncellendi!");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Senkronizasyon hatası: " + e.getMessage());
        }
    }
}
