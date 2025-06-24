package br.com.example.inventoryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class InventoryService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "orders", groupId = "inventory-group")
    public void consumeOrder(Order order) {
        String receivedLog = "[Inventory-Service] Pedido recebido para processar estoque: " + order.getId();
        kafkaTemplate.send("log-events", receivedLog);

        boolean hasStock = new Random().nextBoolean();
        String resultMessage;

        if (hasStock) {
            resultMessage = "[Inventory-Service] SUCESSO: Estoque reservado para o pedido " + order.getId();
        } else {
            resultMessage = "[Inventory-Service] FALHA: Estoque insuficiente para o pedido " + order.getId();
        }
        
        kafkaTemplate.send("log-events", resultMessage);
        kafkaTemplate.send("inventory-events", order.getId(), resultMessage);
    }
}