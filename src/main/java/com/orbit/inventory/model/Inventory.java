package com.orbit.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Long availableQuantity;

    private Long reservedQuantity;
    
    public Inventory() {
		super();
	}

	public Inventory(Long id, Long productId, Long availableQuantity, Long reservedQuantity) {
		super();
		this.id = id;
		this.productId = productId;
		this.availableQuantity = availableQuantity;
		this.reservedQuantity = reservedQuantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@PrePersist
    public void prePersist() {
        if (this.reservedQuantity == null) {
            this.reservedQuantity = 0L;
        }
    }
}
