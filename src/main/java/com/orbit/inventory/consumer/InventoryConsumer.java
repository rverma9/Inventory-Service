package com.orbit.inventory.consumer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbit.inventory.dto.StockOperationDto;
import com.orbit.inventory.service.InventoryService;

@Service
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final Set<String> processedEvents = new HashSet<>();

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "payment-events", groupId = "inventory-group")
    public void updateStock(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            String eventId = root.has("paymentId") 
                    ? "PAY-" + root.get("paymentId").asText() 
                    : "ORDER-" + root.get("orderId").asText();

            if (processedEvents.contains(eventId)) {
                System.out.println(" Duplicate event detected (" + eventId + "). Skipping stock update.");
                return;
            }

            processedEvents.add(eventId);

            String eventType = root.get("eventType").asText();
            JsonNode itemsNode = root.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    Long productId = item.get("productId").asLong();
                    Long qty = item.get("quantity").asLong();
                    StockOperationDto operation = new StockOperationDto();
                    operation.setQuantity(qty);

                    if ("PAYMENT_SUCCESS".equals(eventType)) {
                        inventoryService.decreaseStock(productId, operation);
                        System.out.println(" Stock decreased by " + operation + " for product " + productId);
                    } else if ("PAYMENT_REFUNDED".equals(eventType)) {
                        inventoryService.increaseStock(productId, operation);
                        System.out.println("Stock restored by " + operation + " for product " + productId);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error adjusting stock from Kafka event: " + e.getMessage());
        }
    }
}