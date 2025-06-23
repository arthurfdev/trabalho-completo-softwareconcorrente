package br.com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // Importe esta linha
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*") // <-- ADICIONE ESTA LINHA
@RestController
@RequestMapping("/orders")
public class OrderController {
    // ... o resto do seu código continua igual
    @Autowired
    private OrderProducerService producerService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody List<String> items) {
        Order newOrder = new Order(items);
        producerService.sendOrder(newOrder);
        return ResponseEntity.ok("Pedido recebido com sucesso! ID: " + newOrder.getId());
    }
}