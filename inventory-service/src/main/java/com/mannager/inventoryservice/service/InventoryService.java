package com.mannager.inventoryservice.service;

import com.mannager.inventoryservice.dto.InventoryResponse;
import com.mannager.inventoryservice.entity.Inventory;
import com.mannager.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> skuCodeList){
        return inventoryRepository.findBySkuCodeIn(skuCodeList)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public void create(List<Inventory> inventoryList){
        inventoryRepository.saveAll(inventoryList);
    }

    private InventoryResponse mapToDto(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() >= 1)
                .build();
    }
}
