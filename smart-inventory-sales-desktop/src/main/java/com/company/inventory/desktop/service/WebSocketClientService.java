package com.company.inventory.desktop.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WebSocketClientService {

    public void connectAndSubscribe() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Kritik Stok Uyarısı");
            alert.setHeaderText("Stok Durumu Hakkında Bilgilendirme");
            alert.setContentText("Bir ürünün stoğu kritik seviyeye düştü!");
            
            alert.show();
        });
    }
}