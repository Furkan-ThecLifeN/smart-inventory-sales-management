package com.company.inventory.desktop.repository;

import com.company.inventory.desktop.config.DatabaseConfig;
import com.company.inventory.desktop.model.LocalCustomerDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerLocalRepository {

    public List<LocalCustomerDto> getPendingSyncs() {
        List<LocalCustomerDto> list = new ArrayList<>();
        String sql = "SELECT * FROM local_customers WHERE sync_status = 'PENDING'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(LocalCustomerDto.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .syncStatus(rs.getString("sync_status"))
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStatus(Long id, String status) {
        String sql = "UPDATE local_customers SET sync_status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}