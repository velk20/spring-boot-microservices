package com.angel.microservices.order.service;

import com.angel.microservices.order.client.InventoryClient;
import com.angel.microservices.order.dto.OrderRequest;
import com.angel.microservices.order.model.Order;
import com.angel.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        var inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (inStock) {
            //map OrderRequest to Order object
            Order order = Order
                    .builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();

            //save to repository
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is not in stock");
        }

    }
}
