package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.io.CartRequest;
import com.vn.tan.foodiesapi.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest cartRequest);
    CartResponse getCart();
    void clearCart();
    CartResponse removeFromCart(CartRequest cartRequest);
}
