package com.orbit.inventory.service;

import java.util.List;

import com.orbit.inventory.dto.InventoryRequestDto;
import com.orbit.inventory.dto.InventoryResponseDto;
import com.orbit.inventory.dto.StockOperationDto;
import com.orbit.inventory.exception.InventoryRecordNotFound;

public interface InventoryService {

	List<InventoryResponseDto> getAllInventory();

    InventoryResponseDto getInventoryByProductId(Long productId) throws InventoryRecordNotFound;

    InventoryResponseDto createInventory(InventoryRequestDto request);

    InventoryResponseDto updateInventory(Long productId, InventoryRequestDto request) throws InventoryRecordNotFound;

    InventoryResponseDto increaseStock(Long productId, StockOperationDto operation) throws InventoryRecordNotFound;

    InventoryResponseDto decreaseStock(Long productId, StockOperationDto operation) throws InventoryRecordNotFound;
    
    void initializeInventory(Long productId, Long initialQuantity);
}
