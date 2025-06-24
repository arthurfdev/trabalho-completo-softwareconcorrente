package br.com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;
    
    @Autowired
    private KafkaTemplate<String, String> logKafkaTemplate;

    public void sendOrder(Order order) {
        String logMessage = "[Order-Service] Pedido criado e enviado ao Kafka: " + order.toString();
        
        logKafkaTemplate.send("log-events", logMessage);
        kafkaTemplate.send("orders", order.getId(), order);
        LogMessageHandler.sendMessage(logMessage);
    }
}