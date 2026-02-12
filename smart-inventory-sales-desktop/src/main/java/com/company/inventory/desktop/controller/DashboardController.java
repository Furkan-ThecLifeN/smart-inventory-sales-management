package com.company.inventory.desktop.controller;

import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.model.ProductDto;
import com.company.inventory.desktop.service.ProductService;
import com.company.inventory.desktop.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class DashboardController {

    @FXML private Label totalProductsLabel, lowStockLabel, welcomeLabel;
    @FXML private ListView<String> alertListView;
    @FXML private Button productManageBtn;

    @FXML private StackPane modalOverlay;
    @FXML private TextField nameField, skuField, priceField, stockField;

    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        if (UserSession.getUsername() != null) {
            welcomeLabel.setText("Hoş geldiniz, " + UserSession.getUsername());
        }

        if (UserSession.getRoles() != null &&
            UserSession.getRoles().contains("ROLE_SALES") &&
            !UserSession.getRoles().contains("ROLE_ADMIN")) {

            if (productManageBtn != null) {
                productManageBtn.setVisible(false);
            }
        }

        loadSummaryData();
    }

    @FXML
    private void showAddProductModal() {
        modalOverlay.setVisible(true);
    }

    @FXML
    private void closeModal() {
        modalOverlay.setVisible(false);
        clearFields();
    }

    @FXML
    private void handleQuickAddProduct() {
        try {
            LocalProductDto newProduct = new LocalProductDto();
            newProduct.setId(System.currentTimeMillis());
            newProduct.setName(nameField.getText());
            newProduct.setSku(skuField.getText());
            newProduct.setPrice(new BigDecimal(priceField.getText()));
            newProduct.setStockQuantity(Integer.parseInt(stockField.getText()));

            productService.saveProduct(newProduct);

            showAlert("Başarılı", "Ürün kaydedildi (Offline mod aktif).");
            closeModal();
            loadSummaryData();

        } catch (Exception e) {
            System.err.println("İşlem yerel veritabanına yönlendirildi.");
        }
    }

    private void loadSummaryData() {
        List<ProductDto> products = productService.getAllProducts();

        if (products != null) {
            totalProductsLabel.setText(String.valueOf(products.size()));

            long lowStock = products.stream()
                    .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() < 10)
                    .count();

            lowStockLabel.setText(String.valueOf(lowStock));

            alertListView.getItems().clear();

            products.stream()
                    .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() < 10)
                    .forEach(p -> alertListView.getItems().add(
                            "KRİTİK: " + p.getName() + " (Stok: " + p.getStockQuantity() + ")"
                    ));

            if (alertListView.getItems().isEmpty()) {
                alertListView.getItems().add("Stok durumu şu an stabil.");
            }
        } else {
            totalProductsLabel.setText("ERR");
            lowStockLabel.setText("ERR");
            alertListView.getItems().clear();
            alertListView.getItems().add("Veri tabanına ulaşılamadı!");
        }
    }

    private void clearFields() {
        nameField.clear();
        skuField.clear();
        priceField.clear();
        stockField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        try {
            UserSession.cleanSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 400, 500));
            stage.setTitle("Smart Inventory - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
