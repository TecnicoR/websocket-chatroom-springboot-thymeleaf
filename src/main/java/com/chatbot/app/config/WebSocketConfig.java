package com.chatbot.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the endpoint that the clients will use to connect to our WebSocket server
        registry.addEndpoint("/chatroom").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Set up a simple in-memory message broker
        config.enableSimpleBroker("/topic");
        // Prefix for messages that are bound for methods annotated with @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }
}