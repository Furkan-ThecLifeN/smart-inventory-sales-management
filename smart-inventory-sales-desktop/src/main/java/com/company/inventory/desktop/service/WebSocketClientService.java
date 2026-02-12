package com.company.inventory.desktop.service;

import com.company.inventory.desktop.util.ConnectivityService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WebSocketClientService {

    private volatile boolean connected = false;

    public void autoReconnectManager() {
        Thread reconnectThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (ConnectivityService.isOnline() && !connected) {
                    try {
                        connectAndSubscribe();
                    } catch (Exception ignored) {
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        reconnectThread.setDaemon(true);
        reconnectThread.start();
    }

    public void connectAndSubscribe() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Kritik Stok Uyarısı");
            alert.setHeaderText("Stok Durumu Hakkında Bilgilendirme");
            alert.setContentText("Bir ürünün stoğu kritik seviyeye düştü!");
            alert.show();
        });
        connected = true;
    }

    public boolean isConnected() {
        return connected;
    }
}
