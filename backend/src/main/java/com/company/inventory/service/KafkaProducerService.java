package com.company.inventory.service;

import com.company.inventory.event.OrderCreatedEvent;
import com.company.inventory.event.StockLowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendStockLowEvent(StockLowEvent event) {
        log.info(">>> PRODUCER: Gönderilen StockLowEvent: {}", event);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("stock-events", event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(">>> PRODUCER: Event gönderildi, offset: {}", result.getRecordMetadata().offset());
            } else {
                log.error(">>> PRODUCER: Event gönderilemedi!", ex);
            }
        });
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        log.info(">>> PRODUCER: Gönderilen OrderCreatedEvent: {}", event);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("order-events", event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(">>> PRODUCER: Sipariş event gönderildi, offset: {}", result.getRecordMetadata().offset());
            } else {
                log.error(">>> PRODUCER: Sipariş event gönderilemedi!", ex);
            }
        });
    }
}