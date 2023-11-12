package com.mannager.orderservice.service;

import com.mannager.orderservice.dto.InventoryResponse;
import com.mannager.orderservice.dto.OrderLineItemDto;
import com.mannager.orderservice.dto.OrderRequest;
import com.mannager.orderservice.entity.Order;
import com.mannager.orderservice.entity.OrderLineItem;
import com.mannager.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemList(
                        orderRequest.getOrderLineItemDtoList()
                                .stream()
                                .map(this::mapDtoToItem)
                                .toList()
                )
                .build();

        List<String> skuCodeList = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();

        String inventoryUrl = "http://inventory-service/api/inventory?" +
                skuCodeList.stream()
                        .map(skuCode -> "skuCode=" + skuCode)
                        .collect(Collectors.joining("&"));

        // call inventory service and place order if in stock
        ResponseEntity<InventoryResponse[]> response = restTemplate.getForEntity(inventoryUrl,
                InventoryResponse[].class
        );
        
        boolean allProductsInStock = Arrays.stream(response.getBody())
                .allMatch(InventoryResponse::getIsInStock);

        System.out.println(Arrays.toString(response.getBody()));

        if(allProductsInStock){
            orderRepository.save(order);
            log.info("Order {} placed.", order.getId());
        } else{
            throw new IllegalArgumentException("All products are not in stock");
        }
    }

    private OrderLineItem mapDtoToItem(OrderLineItemDto orderLineItemDto){
        return OrderLineItem.builder()
                .skuCode(orderLineItemDto.getSkuCode())
                .price(orderLineItemDto.getPrice())
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }
}
