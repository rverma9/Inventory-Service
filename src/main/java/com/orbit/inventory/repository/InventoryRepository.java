package com.orbit.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.orbit.inventory.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);
	
}
