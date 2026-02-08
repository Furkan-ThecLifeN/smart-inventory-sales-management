package com.company.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    public void logNotification(String message) {
        log.info("Sistem Bildirimi: {}", message);
        // Burada ileride DB kaydı yapılabilir.
    }
}