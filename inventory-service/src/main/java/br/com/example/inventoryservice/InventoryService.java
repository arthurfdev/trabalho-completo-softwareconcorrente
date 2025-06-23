package br.com.example.inventoryservice;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String RESULT_TOPIC = "inventory-events"; // Tópico de resultado 

    // Anotação que transforma este método em um consumidor do Kafka 
    @KafkaListener(topics = "orders", groupId = "inventory-group")
    public void consumeOrder(Order order) {
        System.out.println("Pedido recebido para processar estoque: " + order.getId());

        // Simulação da lógica de negócio: sucesso ou falha aleatória 
        boolean hasStock = new Random().nextBoolean();
        String resultMessage;

        if (hasStock) {
            resultMessage = "SUCESSO: Estoque reservado para o pedido " + order.getId();
            System.out.println(resultMessage);
        } else {
            resultMessage = "FALHA: Estoque insuficiente para o pedido " + order.getId();
            System.out.println(resultMessage);
        }

        // Publica o resultado no tópico 'inventory-events' 
        kafkaTemplate.send(RESULT_TOPIC, order.getId(), resultMessage);
    }
}