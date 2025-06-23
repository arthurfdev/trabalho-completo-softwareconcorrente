package br.com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

    // O Spring injeta o KafkaTemplate configurado automaticamente
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private final String TOPIC = "orders"; // Tópico definido no requisito 

    public void sendOrder(Order order) {
        // Envia a mensagem. A chave é o ID do pedido e o valor é o objeto Order.
        // O Spring Boot irá serializar o objeto Order para JSON automaticamente.
        kafkaTemplate.send(TOPIC, order.getId(), order);
        System.out.println("Pedido enviado ao Kafka: " + order.toString());
    }
}