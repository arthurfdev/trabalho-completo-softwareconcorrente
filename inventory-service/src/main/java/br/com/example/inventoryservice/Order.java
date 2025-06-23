package br.com.example.inventoryservice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Representa a estrutura de dados de um pedido 
public class Order {

    private String id;
    private long timestamp;
    private List<String> items;

    // Construtor vazio necessário para a desserialização
    public Order() {}

    public Order(List<String> items) {
        this.id = UUID.randomUUID().toString(); // Gera um UUID, conforme o requisito 
        this.timestamp = Instant.now().toEpochMilli(); // Gera um timestamp, conforme o requisito 
        this.items = items;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    @Override
    public String toString() {
        return "Order{" + "id='" + id + '\'' + ", items=" + items + '}';
    }
}