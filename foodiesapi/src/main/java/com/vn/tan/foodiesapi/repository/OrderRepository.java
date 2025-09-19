package com.vn.tan.foodiesapi.repository;

import com.vn.tan.foodiesapi.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByTxnRef(String txnRef);
    List<Order> findByUserId(String userId);
}
