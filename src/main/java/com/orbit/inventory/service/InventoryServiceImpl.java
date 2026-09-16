package com.orbit.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orbit.inventory.client.ProductClient;
import com.orbit.inventory.dto.InventoryRequestDto;
import com.orbit.inventory.dto.InventoryResponseDto;
import com.orbit.inventory.dto.ProductClientResponseDto;
import com.orbit.inventory.dto.StockOperationDto;
import com.orbit.inventory.exception.InventoryRecordNotFound;
import com.orbit.inventory.model.Inventory;
import com.orbit.inventory.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductClient productClient) {
        this.inventoryRepository = inventoryRepository;
        this.productClient = productClient;
    }

    @Override
    public List<InventoryResponseDto> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        List<InventoryResponseDto> responseList = new ArrayList<>();

        for (Inventory inventory : inventories) {
            responseList.add(enrichAndMapToDto(inventory));
        }

        return responseList;
    }

    @Override
    public InventoryResponseDto getInventoryByProductId(Long productId) throws InventoryRecordNotFound {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryRecordNotFound("Inventory record not found for productId: " + productId));
        return enrichAndMapToDto(inventory);
    }

    @Override
    @Transactional
    public InventoryResponseDto createInventory(InventoryRequestDto request) {
        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("Inventory already exists for product ID: " + request.getProductId());
        }

        ProductClientResponseDto product = productClient.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + request.getProductId());
        }

        Inventory inventory = mapToEntity(request);
        Inventory saved = inventoryRepository.save(inventory);

        return mapToDto(saved, product);
    }

    @Override
    @Transactional
    public InventoryResponseDto updateInventory(Long productId, InventoryRequestDto request) throws InventoryRecordNotFound {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryRecordNotFound("Inventory not found for productId: " + productId));

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        if (request.getReservedQuantity() != null) {
            inventory.setReservedQuantity(request.getReservedQuantity());
        }

        Inventory updated = inventoryRepository.save(inventory);
        return enrichAndMapToDto(updated);
    }

    @Override
    @Transactional
    public InventoryResponseDto increaseStock(Long productId, StockOperationDto operation) throws InventoryRecordNotFound {
        if (operation.getQuantity() == null || operation.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryRecordNotFound("Inventory not found for productId: " + productId));

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + operation.getQuantity());
        Inventory updated = inventoryRepository.save(inventory);
        return enrichAndMapToDto(updated);
    }

    @Override
    @Transactional
    public InventoryResponseDto decreaseStock(Long productId, StockOperationDto operation) throws InventoryRecordNotFound {
        if (operation.getQuantity() == null || operation.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryRecordNotFound("Inventory not found for productId: " + productId));

        if (inventory.getAvailableQuantity() < operation.getQuantity()) {
            throw new RuntimeException("Out of stock!");
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - operation.getQuantity());
        Inventory updated = inventoryRepository.save(inventory);
        return enrichAndMapToDto(updated);
    }

    private Inventory mapToEntity(InventoryRequestDto dto) {
        Inventory inventory = new Inventory();
        inventory.setProductId(dto.getProductId());
        inventory.setAvailableQuantity(dto.getAvailableQuantity());
        inventory.setReservedQuantity(dto.getReservedQuantity() != null ? dto.getReservedQuantity() : 0);
        return inventory;
    }

    private InventoryResponseDto enrichAndMapToDto(Inventory inventory) {
        ProductClientResponseDto product = null;
        try {
            product = productClient.getProductById(inventory.getProductId());
        } catch (Exception ex) {
            log.error("Failed to fetch product details for productId {}: {}", inventory.getProductId(), ex.getMessage());
        }
        return mapToDto(inventory, product);
    }
    
    @Override
    public void initializeInventory(Long productId, Long initialQuantity) {
        if (inventoryRepository.findByProductId(productId).isPresent()) {
            return; // Already initialized
        }
        
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setAvailableQuantity(initialQuantity != null ? initialQuantity : 0L);
        inventory.setReservedQuantity(0L);
        
        inventoryRepository.save(inventory);
    }

    private InventoryResponseDto mapToDto(Inventory inventory, ProductClientResponseDto product) {
        InventoryResponseDto dto = new InventoryResponseDto();
        dto.setInventoryId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());

        if (product != null) {
            dto.setProductName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setCategory(product.getCategory());
        } else {
            dto.setProductName("Unknown / Product Service Unavailable");
            dto.setPrice(null);
            dto.setCategory(null);
        }

        return dto;
    }
}