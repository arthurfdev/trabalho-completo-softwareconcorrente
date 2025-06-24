package br.com.example.orderservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogConsumerService {

    @KafkaListener(topics = "log-events", groupId = "log-group")
    public void consumeLogEvent(String message) {
        LogMessageHandler.sendMessage(message);
    }
}