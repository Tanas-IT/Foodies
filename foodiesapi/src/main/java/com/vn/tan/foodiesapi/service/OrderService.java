package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.entities.Order;
import com.vn.tan.foodiesapi.io.OrderRequest;
import com.vn.tan.foodiesapi.io.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public Order create(Order order);

    public Optional<Order> findByOrderById(String orderId);

    public Order update(Order order);
    public Order createFromRequest(OrderRequest request);
    public List<OrderResponse> getUserOrders();
    void removeOrder(String orderId);
    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId, String status);
}
