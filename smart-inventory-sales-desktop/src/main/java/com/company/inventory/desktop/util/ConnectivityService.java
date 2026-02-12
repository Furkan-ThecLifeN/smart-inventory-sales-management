package com.company.inventory.desktop.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectivityService {

    private static volatile boolean online = false;
    private static final String HEALTH_URL = "http://localhost:8080/api/products";

    public static void startMonitoring() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                URL url = new URL(HEALTH_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                online = (responseCode >= 200 && responseCode < 500);

                conn.disconnect();
            } catch (Exception e) {
                online = false;
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static boolean isOnline() {
        return online;
    }
}
