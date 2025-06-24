package br.com.example.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consumeInventoryEvent(String message) {
        String log1 = "[Notification-Service] Evento de inventário recebido.";
        String log2 = "[Notification-Service] Enviando notificação (E-mail/SMS) para o usuário...";
        String log3 = "[Notification-Service] Conteúdo: " + message;
        
        kafkaTemplate.send("log-events", log1);
        kafkaTemplate.send("log-events", log2);
        kafkaTemplate.send("log-events", log3);
    }
}