package com.vn.tan.foodiesapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPayConfig {
    @Value("${vnpay.url}")
    private String url;
    @Value("${vnpay.return-url}")
    private String returnUrl;
    @Value("${vnpay.tmn-code}")
    private String tmnCode;
    @Value("${vnpay.hash-secret}")
    private String hashSecret;
    @Value("${vnpay.ipAddress}")
    private String ipAddress;

    @Value("${vnpay.payment-return-fe}")
    private String paymentReturnToFE;

}
