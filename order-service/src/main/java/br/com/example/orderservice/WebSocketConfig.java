package br.com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired; // <-- Importe esta linha
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Injeta o LogMessageHandler gerenciado pelo Spring
    @Autowired
    private LogMessageHandler logMessageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Usa a instância injetada em vez de criar uma nova
        registry.addHandler(logMessageHandler, "/ws/logs")
                .setAllowedOrigins("*");
    }
}