package com.company.inventory.desktop.repository;

import com.company.inventory.desktop.config.DatabaseConfig;
import com.company.inventory.desktop.model.LocalProductDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductLocalRepository {

    public void save(LocalProductDto product) throws Exception {
        String sql = "INSERT OR REPLACE INTO local_products (id, name, sku, price, stock_quantity, sync_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getSku());
            pstmt.setDouble(4, product.getPrice().doubleValue());
            pstmt.setInt(5, product.getStockQuantity());
            pstmt.setString(6, product.getSyncStatus());
            pstmt.executeUpdate();
        }
    }

    public List<LocalProductDto> getPendingSyncs() throws Exception {
        List<LocalProductDto> list = new ArrayList<>();
        String sql = "SELECT * FROM local_products WHERE sync_status = 'PENDING'";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalProductDto product = new LocalProductDto();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setSku(rs.getString("sku"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setSyncStatus(rs.getString("sync_status"));
                list.add(product);
            }
        }
        return list;
    }

    public List<LocalProductDto> getAll() throws Exception {
        List<LocalProductDto> list = new ArrayList<>();
        String sql = "SELECT * FROM local_products";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalProductDto product = new LocalProductDto();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setSku(rs.getString("sku"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setSyncStatus(rs.getString("sync_status"));
                list.add(product);
            }
        }
        return list;
    }
}
