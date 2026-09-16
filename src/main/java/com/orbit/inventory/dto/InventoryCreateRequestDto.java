package com.orbit.inventory.dto;

public class InventoryCreateRequestDto {
	
    private Long productId;
    private Long initialQuantity;

    public InventoryCreateRequestDto() {}

    public InventoryCreateRequestDto(Long productId, Long initialQuantity) {
        this.productId = productId;
        this.initialQuantity = initialQuantity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(Long initialQuantity) { this.initialQuantity = initialQuantity; }
}
