package com.orbit.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.orbit.inventory.dto.ProductClientResponseDto;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductClientResponseDto getProductById(@PathVariable("id") Long id);
}