package com.company.inventory.desktop;

import com.company.inventory.desktop.config.DatabaseConfig;
import com.company.inventory.desktop.sync.SyncManager;
import com.company.inventory.desktop.util.ConnectivityService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryDesktopApplication extends Application {

    private final SyncManager syncManager = new SyncManager();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start(Stage stage) {
        try {
            // 1. Yerel Veritabanı Şemasını Başlat
            DatabaseConfig.initializeSchema();

            // 2. Bağlantı İzleyiciyi Başlat (İnternet Geldiğinde Tetikler)
            ConnectivityService.startMonitoring(isOnline -> {
                if (isOnline) {
                    System.out.println("🌐 Bağlantı sağlandı! Otomatik senkronizasyon tetikleniyor...");
                    // UI thread'ini kilitlememek için ayrı bir thread'de çalıştırır
                    syncManager.performSync();
                } else {
                    System.out.println("🔌 Bağlantı kesildi. Offline mod aktif.");
                }
            });

            // 3. Periyodik Kontrol (Her 30 saniyede bir sessizce kontrol eder)
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    syncManager.performSync();
                } catch (Exception e) {
                    System.err.println("❌ Periyodik Sync sırasında beklenmedik hata:");
                    e.printStackTrace(); // Hatayı terminalde görmek için kritik
                }
            }, 10, 30, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("❌ Başlatma hatası:");
            e.printStackTrace();
        }

        // 4. UI (Login Sayfası) Yükleme
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            stage.setTitle("Smart Inventory - Kurumsal Masaüstü");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("UI Yüklenemedi!", e);
        }
    }

    @Override
    public void stop() throws Exception {
        // Uygulama kapanırken arka plan görevlerini kibarca durdur
        scheduler.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}