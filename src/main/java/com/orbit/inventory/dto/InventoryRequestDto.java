package com.orbit.inventory.dto;

public class InventoryRequestDto {

    private Long productId;
    private Long availableQuantity;
    private Long reservedQuantity;
    
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public Long getReservedQuantity() {
		return reservedQuantity;
	}
	public void setReservedQuantity(Long reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}
}
