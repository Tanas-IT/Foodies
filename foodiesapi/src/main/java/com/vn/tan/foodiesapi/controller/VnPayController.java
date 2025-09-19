package com.vn.tan.foodiesapi.controller;

import com.vn.tan.foodiesapi.controller.routes.OrderRoute;
import com.vn.tan.foodiesapi.entities.Order;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.OrderRequest;
import com.vn.tan.foodiesapi.service.OrderService;
import com.vn.tan.foodiesapi.service.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {
    private final VnPayService vnPayService;
    private final OrderService orderService;

    @Autowired
    public VnPayController(VnPayService vnPayService, OrderService orderService) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
    }


    @PostMapping("/create-payment")
    public String createPayment(@RequestBody OrderRequest orderRequest) {
        try {
            return vnPayService.createPayment(orderRequest.getAmount(), orderRequest);
        } catch (Exception ex) {
            throw new RuntimeException("Error creating VNPay payment", ex);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse> vnpayReturn(@RequestParam Map<String,String> allParams) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Payment Successfully");
            apiResponse.setData(vnPayService.vnpayReturn(allParams));
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @PostMapping("/ipn")
    public String vnpayIpn(@RequestParam Map<String, String> allParams) {
        try {
            return vnPayService.vnpayIpn(allParams);
        } catch (Exception ex) {
            return "99";
        }
    }
}
