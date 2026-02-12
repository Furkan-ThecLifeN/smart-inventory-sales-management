package com.company.inventory.desktop;

import com.company.inventory.desktop.config.DatabaseConfig;
import com.company.inventory.desktop.sync.SyncManager;
import com.company.inventory.desktop.util.ConnectivityService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InventoryDesktopApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            DatabaseConfig.initializeSchema();
            ConnectivityService.startMonitoring();

            SyncManager syncManager = new SyncManager();
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    syncManager::performSync,
                    5,
                    15,
                    TimeUnit.SECONDS
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            stage.setTitle("Smart Inventory - Desktop Client");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("UI initialization failed", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
