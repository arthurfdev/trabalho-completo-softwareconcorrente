package br.com.example.notificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Ouve o tópico 'inventory-events' 
    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consumeInventoryEvent(String message) {
        System.out.println("--- NOTIFICAÇÃO ---");
        System.out.println("Enviando notificação (E-mail/SMS) para o usuário...");
        System.out.println("Conteúdo: " + message); // Registra no console 
        System.out.println("--------------------");
    }
}