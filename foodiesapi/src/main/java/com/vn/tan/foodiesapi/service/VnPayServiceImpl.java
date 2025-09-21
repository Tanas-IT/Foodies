package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.config.VnPayConfig;
import com.vn.tan.foodiesapi.entities.Order;
import com.vn.tan.foodiesapi.io.OrderRequest;
import com.vn.tan.foodiesapi.repository.OrderRepository;
import com.vn.tan.foodiesapi.util.VnPayUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VnPayServiceImpl implements VnPayService{
    private final OrderRepository orderRepository;
    private final VnPayConfig vnPayConfig;
    private final UserService userService;

    @Autowired
    public VnPayServiceImpl(OrderRepository orderRepository, VnPayConfig vnPayConfig, UserService userService) {
        this.orderRepository = orderRepository;
        this.vnPayConfig = vnPayConfig;
        this.userService = userService;
    }

    @Override
    public String createPayment(Long amount, OrderRequest orderRequest) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TmnCode = vnPayConfig.getTmnCode();
            String vnp_Locale = "vn";
            String vnp_CurrCode = "VND";
            String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "");
            String vnp_OrderInfo = "Payment foodies";
            String vnp_OrderType = "other";
            // VNPay yêu cầu amount * 100 (ví dụ 10000 VND -> 1000000)
            String vnp_Amount = String.valueOf(amount * 100);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String vnp_CreateDate = LocalDateTime.now().format(dtf);

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", vnp_Version);
            vnpParams.put("vnp_Command", vnp_Command);
            vnpParams.put("vnp_TmnCode", vnp_TmnCode);
            vnpParams.put("vnp_Amount", vnp_Amount);
            vnpParams.put("vnp_CurrCode", vnp_CurrCode);
            vnpParams.put("vnp_IpAddr", vnPayConfig.getIpAddress());
            vnpParams.put("vnp_TxnRef", vnp_TxnRef);
            vnpParams.put("vnp_OrderInfo", vnp_OrderInfo);
            vnpParams.put("vnp_OrderType", vnp_OrderType);
            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnpParams.put("vnp_CreateDate", vnp_CreateDate);
            vnpParams.put("vnp_Locale", vnp_Locale);

            // Bước 1: build query string cho URL (đã encode)
            String query = VnPayUitl.buildQueryString(vnpParams, false);

            // Bước 2: build hash data (không encode, không chứa SecureHash/Type)
            String hashData = VnPayUitl.buildQueryString(vnpParams, true);

            // Bước 3: ký bằng HMAC-SHA512
            String secureHash = VnPayUitl.hmacSHA512(vnPayConfig.getHashSecret(), hashData);

            // Bước 4: gắn SecureHash + SecureHashType vào URL
            String paymentUrl = vnPayConfig.getUrl()
                    + "?" + query
                    + "&vnp_SecureHashType=SHA512"
                    + "&vnp_SecureHash=" + secureHash;

            // Lưu order ban đầu với trạng thái CREATED (chưa thanh toán)
            String loggedUserId = userService.findByUserId();
            Order order = new Order();
            order.setTxnRef(vnp_TxnRef);
            order.setAmount(amount); // lưu VND
            order.setOrderStatus(orderRequest.getOrderStatus());
            order.setAmount(orderRequest.getAmount());
            order.setFullName(orderRequest.getFullName());
            order.setPhoneNumber(orderRequest.getPhoneNumber());
            order.setOrderedItems(orderRequest.getOrderItemList());
            order.setUserAddress(orderRequest.getUserAddress());
            order.setUserId(loggedUserId);
            order.setEmail(orderRequest.getEmail());
            order.setPaymentStatus("PENDING");
            orderRepository.save(order);

            // Redirect user sang VNPay
            return paymentUrl;
        } catch (Exception ex) {
            throw new RuntimeException("Error creating VNPay payment", ex);
        }
    }

    @Override
    public String vnpayReturn(Map<String, String> allParams) throws Exception {
        // allParams chứa các param VNPay gửi về, ví dụ vnp_ResponseCode, vnp_SecureHash, vnp_TxnRef, vnp_TransactionNo, vnp_Amount...
        try {
            String vnpSecureHash = allParams.get("vnp_SecureHash");
            Map<String, String> data = new HashMap<>(allParams);
            data.remove("vnp_SecureHash");
            data.remove("vnp_SecureHashType");

            // Build hashData string sorted by key
            Map<String, String> sorted = new TreeMap<>(data);

            // Nối key=value bằng "&"
            String hashData = sorted.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));


            String calculatedHash = VnPayUitl.hmacSHA512(vnPayConfig.getHashSecret(), hashData);

            String txnRef = allParams.get("vnp_TxnRef");
            String responseCode = allParams.get("vnp_ResponseCode"); // "00" = success

            Optional<Order> maybeOrder = orderRepository.findByTxnRef(txnRef);
            if (maybeOrder.isEmpty()) {
                return "Order not found";
            }
            Order order = maybeOrder.get();

                if ("00".equals(responseCode)) {
                    order.setPaymentStatus("PAID");
                    order.setVnpResponseCode(responseCode);
                    order.setVnpTransactionNo(allParams.get("vnp_TransactionNo"));
                    orderRepository.save(order);
                    // trả về UI hoặc redirect về trang success
                    return order.getId();
                } else {
                    order.setPaymentStatus("FAILED");
                    order.setVnpResponseCode(responseCode);
                    orderRepository.save(order);
                    throw new Exception("Payment failed. Code: " + responseCode);
                }
        } catch (Exception ex) {
            throw new Exception("Error processing return: " + ex.getMessage());
        }
    }

    @Override
    public String vnpayIpn(Map<String, String> allParams) {
        try {
            String vnpSecureHash = allParams.get("vnp_SecureHash");
            Map<String, String> data = new HashMap<>(allParams);
            data.remove("vnp_SecureHash");
            data.remove("vnp_SecureHashType");

            String hashData = new TreeMap<>(data)
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((a,b) -> a + "&" + b)
                    .orElse("");

            String calculatedHash = VnPayUitl.hmacSHA512(vnPayConfig.getHashSecret(), hashData);

            String txnRef = allParams.get("vnp_TxnRef");
            String responseCode = allParams.get("vnp_ResponseCode");

            Optional<Order> maybeOrder = orderRepository.findByTxnRef(txnRef);
            if (maybeOrder.isEmpty()) {
                // nếu chưa có order, có thể tạo mới hoặc trả về lỗi
                return "01"; // trả vnp yêu cầu mã lỗi (01 = order not found)
            }
            Order order = maybeOrder.get();

            if (!calculatedHash.equalsIgnoreCase(vnpSecureHash)) {
                return "97"; // Invalid signature
            }

            // Idempotent: nếu đã SUCCESS thì trả về 00 ngay
            if ("SUCCESS".equals(order.getOrderStatus())) {
                return "00";
            }

            if ("00".equals(responseCode)) {
                order.setPaymentStatus("SUCCESS");
                order.setVnpResponseCode(responseCode);
                order.setVnpTransactionNo(allParams.get("vnp_TransactionNo"));
                orderRepository.save(order);
                return "00";
            } else {
                order.setPaymentStatus("FAILED");
                order.setVnpResponseCode(responseCode);
                orderRepository.save(order);
                return "00"; // trả 00 để VNPay không gửi lại (tuỳ logic bạn muốn)
            }
        } catch (Exception ex) {
            return "99";
        }
    }
}
