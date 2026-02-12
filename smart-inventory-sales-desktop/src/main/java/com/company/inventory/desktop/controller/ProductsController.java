package com.company.inventory.desktop.controller;

import com.company.inventory.desktop.repository.ProductLocalRepository;
import com.company.inventory.desktop.model.LocalProductDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductsController {

    @FXML private TableView<LocalProductDto> productTable;
    @FXML private TableColumn<LocalProductDto, String> colName;
    @FXML private TableColumn<LocalProductDto, String> colSku;
    @FXML private TableColumn<LocalProductDto, BigDecimal> colPrice;
    @FXML private TableColumn<LocalProductDto, Integer> colStock;
    @FXML private TableColumn<LocalProductDto, String> colSyncStatus;

    @FXML private TextField txtName;
    @FXML private TextField txtSku;
    @FXML private TextField txtPrice;
    @FXML private TextField txtStock;

    private final ProductLocalRepository localRepository = new ProductLocalRepository();
    private ObservableList<LocalProductDto> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colSyncStatus.setCellValueFactory(new PropertyValueFactory<>("syncStatus"));

        loadProductsFromLocal();
    }

    private void loadProductsFromLocal() {
        try {
            List<LocalProductDto> items = localRepository.getAll();
            productList.setAll(items);
            productTable.setItems(productList);
        } catch (Exception e) {
            showAlert("Hata", "Yerel veriler yüklenemedi: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveProduct() {
        try {
            LocalProductDto newProduct = new LocalProductDto();
            
            newProduct.setId(System.currentTimeMillis());
            newProduct.setName(txtName.getText());
            newProduct.setSku(txtSku.getText());
            newProduct.setPrice(new BigDecimal(txtPrice.getText()));
            newProduct.setStockQuantity(Integer.parseInt(txtStock.getText()));
            
            newProduct.setSyncStatus("PENDING"); 
            newProduct.setLastModifiedAt(LocalDateTime.now());

            localRepository.save(newProduct); 
            
            productList.add(newProduct);
            clearFields();
            
            showAlert("Başarılı", "Ürün kaydedildi. Sistem internet bulduğunda senkronize edilecektir.");
        } catch (Exception e) {
            showAlert("Hata", "İşlem başarısız: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtName.clear();
        txtSku.clear();
        txtPrice.clear();
        txtStock.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}