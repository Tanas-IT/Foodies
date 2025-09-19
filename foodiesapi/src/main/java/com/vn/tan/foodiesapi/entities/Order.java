package com.vn.tan.foodiesapi.entities;

import com.vn.tan.foodiesapi.io.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private String id;
    private String userId;
    private String userAddress;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<OrderItem> orderedItems;
    private double amount;
    private String paymentStatus;
    private String vnpResponseCode;
    private String vnpTransactionNo;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String orderStatus;
    private String txnRef;
}
