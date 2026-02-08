package com.company.inventory.service;

import com.company.inventory.event.OrderCreatedEvent;
import com.company.inventory.event.StockLowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
        topics = "stock-events",
        groupId = "inventory-final-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeStockLowEvent(StockLowEvent event) {
        log.info(">>> CONSUMER: StockLowEvent alındı: {}", event);
        messagingTemplate.convertAndSend("/topic/stock-alerts", event);
    }

    @KafkaListener(
        topics = "order-events",
        groupId = "inventory-final-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info(">>> CONSUMER: OrderCreatedEvent alındı: {}", event);
        messagingTemplate.convertAndSend("/topic/orders", event);
    }
}
