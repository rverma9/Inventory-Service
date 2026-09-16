package com.orbit.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orbit.inventory.dto.InventoryCreateRequestDto;
import com.orbit.inventory.dto.InventoryRequestDto;
import com.orbit.inventory.dto.InventoryResponseDto;
import com.orbit.inventory.dto.StockOperationDto;
import com.orbit.inventory.exception.InventoryRecordNotFound;
import com.orbit.inventory.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDto>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> getInventoryByProductId(@PathVariable Long productId) throws InventoryRecordNotFound {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDto> createInventory(@RequestBody InventoryRequestDto request) {
        return new ResponseEntity<>(inventoryService.createInventory(request), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> updateInventory(@PathVariable Long productId, @RequestBody InventoryRequestDto request) throws InventoryRecordNotFound {
        return ResponseEntity.ok(inventoryService.updateInventory(productId, request));
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<InventoryResponseDto> increaseStock(@PathVariable Long productId, @RequestBody StockOperationDto operation) throws InventoryRecordNotFound {
        return ResponseEntity.ok(inventoryService.increaseStock(productId, operation));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<InventoryResponseDto> decreaseStock(@PathVariable Long productId, @RequestBody StockOperationDto operation) throws InventoryRecordNotFound {
        return ResponseEntity.ok(inventoryService.decreaseStock(productId, operation));
    }
    
    @PostMapping("/init")
    public ResponseEntity<Void> initInventory(@RequestBody InventoryCreateRequestDto request) {
        inventoryService.initializeInventory(request.getProductId(), request.getInitialQuantity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}