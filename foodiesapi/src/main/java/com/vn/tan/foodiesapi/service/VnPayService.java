package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.io.OrderRequest;

import java.util.Map;

public interface VnPayService {
     String createPayment(Long amount, OrderRequest orderRequest);
     String vnpayReturn(Map<String,String> allParams) throws Exception;
     String vnpayIpn( Map<String, String> allParams);
}
