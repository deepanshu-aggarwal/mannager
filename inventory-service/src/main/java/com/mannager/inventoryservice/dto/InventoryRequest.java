package com.mannager.inventoryservice.dto;

import com.mannager.inventoryservice.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private List<Inventory> inventoryList;
}
