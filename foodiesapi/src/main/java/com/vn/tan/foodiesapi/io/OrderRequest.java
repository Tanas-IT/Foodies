package com.vn.tan.foodiesapi.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<OrderItem> orderItemList;
    private String userAddress;
    private Long amount;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String orderStatus;

}
