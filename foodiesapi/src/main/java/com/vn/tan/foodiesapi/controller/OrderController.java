package com.vn.tan.foodiesapi.controller;

import com.vn.tan.foodiesapi.controller.routes.OrderRoute;
import com.vn.tan.foodiesapi.controller.routes.UserRoute;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.OrderResponse;
import com.vn.tan.foodiesapi.io.UserRequest;
import com.vn.tan.foodiesapi.io.UserResponse;
import com.vn.tan.foodiesapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OrderRoute.API_BASE_ORDER)
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrders() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Get Orders Successfully");
            List<OrderResponse> result = this.orderService.getUserOrders();
            apiResponse.setData(result);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> DeleteOrder(@PathVariable("orderId") String orderId) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Delete Order Successfully");
            this.orderService.removeOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    // admin panel
    @GetMapping(OrderRoute.GET_ALL_ORDER)
    public ResponseEntity<ApiResponse> GetAllOrder() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Get All Order Successfully");
            apiResponse.setData(this.orderService.getOrdersOfAllUsers());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    // admin panel
    @PatchMapping(OrderRoute.UPDATE_ORDER_STATUS)
    public ResponseEntity<ApiResponse> UpdateOrderStatus(@PathVariable("orderId") String orderId, @RequestParam String status) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Update Order Successfully");
            this.orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }
}
