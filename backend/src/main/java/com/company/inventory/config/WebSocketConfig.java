package com.company.inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Mesajların istemcilere iletileceği ön ek (Örn: /topic/stock-alerts)
        config.enableSimpleBroker("/topic");
        
        // İstemcilerden sunucuya gönderilecek mesajların ön eki
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // İstemcilerin bağlanacağı ana uç nokta
        registry.addEndpoint("/ws-inventory")
                .setAllowedOriginPatterns("*") // CORS izinleri
                .withSockJS(); // SockJS desteği (URL sonuna /websocket eklemek için gerekli)
    }
}