package com.mannager.inventoryservice.controller;

import com.mannager.inventoryservice.dto.InventoryRequest;
import com.mannager.inventoryservice.dto.InventoryResponse;
import com.mannager.inventoryservice.entity.Inventory;
import com.mannager.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.create(inventoryRequest.getInventoryList());
    }
}
