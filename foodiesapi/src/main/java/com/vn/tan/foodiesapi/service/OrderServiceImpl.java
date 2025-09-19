package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.config.VnPayConfig;
import com.vn.tan.foodiesapi.entities.Order;
import com.vn.tan.foodiesapi.io.OrderRequest;
import com.vn.tan.foodiesapi.io.OrderResponse;
import com.vn.tan.foodiesapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final VnPayConfig vnPayConfig;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, VnPayConfig vnPayConfig, UserService userService) {
        this.orderRepository = orderRepository;
        this.vnPayConfig = vnPayConfig;
        this.userService = userService;
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findByOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order createFromRequest(OrderRequest request) {
        Order order =  covertToEntity(request);
       return this.orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedUserId = userService.findByUserId();
        List<Order> listOrder = orderRepository.findByUserId(loggedUserId);
        return listOrder.stream().map(entity -> covertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<Order> list = this.orderRepository.findAll();
        return list.stream().map(entity -> covertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private Order covertToEntity(OrderRequest request) {
       return Order.builder()
               .amount(request.getAmount())
               .email(request.getEmail())
               .orderStatus(request.getOrderStatus())
               .userAddress(request.getUserAddress())
               .build();

    }
    private OrderResponse covertToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .userAddress(order.getUserAddress())
                .userId(order.getUserId())
                .txnRef(order.getTxnRef())
                .paymentStatus(order.getPaymentStatus())
                .email(order.getEmail())
                .orderStatus(order.getOrderStatus())
                .orderedItems(order.getOrderedItems())
                .fullName(order.getFullName())
                .build();

    }

}
