package com.company.inventory.desktop.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Sistemin internet/backend bağlantısını sürekli izleyen merkezi servis.
 */
public class ConnectivityService {

    private static volatile boolean online = false;
    
    // Actuator kullanmak en garantisidir, çünkü backend tam hazır olduğunda yanıt verir.
    private static final String HEALTH_URL = "http://localhost:8080/actuator/health";
    
    private static Consumer<Boolean> onStatusChange;

    /**
     * İzleme işlemini başlatır.
     * @param callback Bağlantı durumu her değiştiğinde (true->false veya false->true) tetiklenir.
     */
    public static void startMonitoring(Consumer<Boolean> callback) {
        onStatusChange = callback;
        
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            boolean previousStatus = online;
            try {
                URL url = new URL(HEALTH_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000); // 3 saniye idealdir
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                // 2xx ve 4xx kodları server'ın yaşadığını gösterir (Security engellese bile server açıktır)
                online = (responseCode >= 200 && responseCode < 500);
                
                conn.disconnect();
            } catch (Exception e) {
                online = false;
            }

            // Durum değiştiyse (örneğin internet geldiyse) haberdar et
            if (online != previousStatus && onStatusChange != null) {
                onStatusChange.accept(online);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static boolean isOnline() {
        return online;
    }
}