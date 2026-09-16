package com.orbit.inventory.dto;

public class InventoryResponseDto {
	
    private Long inventoryId;
    private Long productId;
    private String productName;
    private Double price;
    private String category;
    private Long availableQuantity;
    private Long reservedQuantity;
    
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
