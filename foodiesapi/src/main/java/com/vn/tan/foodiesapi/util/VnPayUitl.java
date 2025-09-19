package com.vn.tan.foodiesapi.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class VnPayUitl {
    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512"); // đúng SHA512
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while calculating HMACSHA512", ex);
        }
    }

    public static String buildQueryString(Map<String, String> params, boolean forHash) {
        SortedMap<String, String> sorted = new TreeMap<>(params);
        StringBuilder query = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : sorted.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) continue;

            // Nếu đang build chuỗi để hash -> bỏ qua vnp_SecureHash & vnp_SecureHashType
            if (forHash && (entry.getKey().equals("vnp_SecureHash") || entry.getKey().equals("vnp_SecureHashType"))) {
                continue;
            }

            if (!first) {
                query.append('&');
            }
            first = false;

            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
        }
        return query.toString();
    }
}
