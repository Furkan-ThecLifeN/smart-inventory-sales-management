package com.company.inventory.desktop.controller;

import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.model.ProductDto;
import com.company.inventory.desktop.model.LocalCustomerDto;
import com.company.inventory.desktop.service.ProductService;
import com.company.inventory.desktop.service.CustomerService;
import com.company.inventory.desktop.sync.SyncManager;
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

    // Ürün Modalı
    @FXML private StackPane modalOverlay;
    @FXML private TextField nameField, skuField, priceField, stockField;

    // Müşteri Modalı
    @FXML private StackPane customerModalOverlay;
    @FXML private TextField customerNameField, customerEmailField, customerPhoneField;

    private final ProductService productService = new ProductService();
    private final CustomerService customerService = new CustomerService();
    private final SyncManager syncManager = new SyncManager(); // SyncManager eklendi

    @FXML
    public void initialize() {
        if (UserSession.getUsername() != null) {
            welcomeLabel.setText("Hoş geldiniz, " + UserSession.getUsername());
        }

        // Satış personeli kısıtlaması
        if (UserSession.getRoles() != null &&
                UserSession.getRoles().contains("ROLE_SALES") &&
                !UserSession.getRoles().contains("ROLE_ADMIN")) {
            if (productManageBtn != null) productManageBtn.setVisible(false);
        }

        loadSummaryData();
        handleSync(); // Uygulama açılışında bir kez sync dene
    }

    // --- ÜRÜN İŞLEMLERİ ---
    @FXML
    private void showAddProductModal() { modalOverlay.setVisible(true); }

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
            System.err.println("Ürün ekleme hatası: " + e.getMessage());
        }
    }

    // --- MÜŞTERİ İŞLEMLERİ ---
    @FXML
    private void showAddCustomerModal() { customerModalOverlay.setVisible(true); }

    @FXML
    private void closeCustomerModal() { 
        customerModalOverlay.setVisible(false); 
        clearCustomerFields(); 
    }

    @FXML
    private void handleSaveCustomer() {
        try {
            LocalCustomerDto customer = new LocalCustomerDto();
            customer.setName(customerNameField.getText());
            customer.setEmail(customerEmailField.getText());
            customer.setPhone(customerPhoneField.getText());

            customerService.saveCustomer(customer);
            showAlert("İşlem Başarılı", "Müşteri yerel veritabanına kaydedildi.");
            closeCustomerModal();
        } catch (Exception e) {
            System.err.println("Müşteri kaydedilirken hata: " + e.getMessage());
        }
    }

    // --- SYNC VE DİĞERLERİ ---
    @FXML
    private void handleSync() {
        // Tek tek servisler yerine merkezi SyncManager'ı tetikliyoruz
        syncManager.performSync();
        loadSummaryData(); // Veriler gelmiş olabilir, özeti yenile
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
                    .forEach(p -> alertListView.getItems().add("KRİTİK: " + p.getName() + " (Stok: " + p.getStockQuantity() + ")"));

            if (alertListView.getItems().isEmpty()) alertListView.getItems().add("Stok durumu şu an stabil.");
        }
    }

    private void clearFields() {
        nameField.clear(); skuField.clear(); priceField.clear(); stockField.clear();
    }

    private void clearCustomerFields() {
        customerNameField.clear(); customerEmailField.clear(); customerPhoneField.clear();
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
        } catch (IOException e) { e.printStackTrace(); }
    }
}