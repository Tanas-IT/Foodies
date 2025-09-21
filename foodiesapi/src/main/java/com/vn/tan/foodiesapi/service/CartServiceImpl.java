package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.entities.Cart;
import com.vn.tan.foodiesapi.io.CartRequest;
import com.vn.tan.foodiesapi.io.CartResponse;
import com.vn.tan.foodiesapi.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    public CartServiceImpl(UserService userService, CartRepository cartRepository) {
        this.userService = userService;
        this.cartRepository = cartRepository;
    }
    @Override
    public CartResponse addToCart(CartRequest cartRequest) {
        String loggedInUserId = userService.findByUserId();
        Optional<Cart> cartOptional = cartRepository.findByUserId(loggedInUserId);
        Cart cart = cartOptional.orElseGet(() -> new Cart(loggedInUserId, new HashMap<>()));
        Map<String, Integer> cartItems =  cart.getItems();
        cartItems.put(cartRequest.getFoodId(), cartItems.getOrDefault(cartRequest.getFoodId(), 0) + 1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
       return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();
        Cart cart = cartRepository.findByUserId(loggedInUserId)
                .orElse(new Cart(null, loggedInUserId, new HashMap<>()));
        return convertToResponse(cart);
    }

    @Override
    public void clearCart() {
        String loggedUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest cartRequest) {
       String loggedUserId = userService.findByUserId();
       Cart cart = cartRepository.findByUserId(loggedUserId).orElseThrow(() -> new RuntimeException("Cart is not found"));
       Map<String, Integer> cartItems = cart.getItems();
       if(cartItems.containsKey(cartRequest.getFoodId())) {
           int currentQuantity = cartItems.get(cartRequest.getFoodId());
           if(currentQuantity > 0) {
               cartItems.put(cartRequest.getFoodId(), currentQuantity - 1);
           } else {
               cartItems.remove(cartRequest.getFoodId());
           }
           cart = cartRepository.save(cart);
           convertToResponse(cart);
       }
       return convertToResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(CartRequest cartRequest) {
        String loggedUserId = userService.findByUserId();
        Cart cart = cartRepository.findByUserId(loggedUserId).orElseThrow(() -> new RuntimeException("Cart is not found"));
        Map<String, Integer> cartItems = cart.getItems();
        if(cartItems.containsKey(cartRequest.getFoodId())) {
            cartItems.remove(cartRequest.getFoodId());
            cart = cartRepository.save(cart);
            convertToResponse(cart);
        }
        return convertToResponse(cart);
    }

    private CartResponse convertToResponse(Cart cart) {
      return CartResponse.builder()
                .id(cart.getId())
              .userId(cart.getUserId())
                .items(cart.getItems()).build();
    }

}
