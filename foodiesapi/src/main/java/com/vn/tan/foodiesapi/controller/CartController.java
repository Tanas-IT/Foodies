package com.vn.tan.foodiesapi.controller;

import com.vn.tan.foodiesapi.controller.routes.CartRoute;
import com.vn.tan.foodiesapi.controller.routes.UserRoute;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.CartRequest;
import com.vn.tan.foodiesapi.io.CartResponse;
import com.vn.tan.foodiesapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(CartRoute.API_BASE_CART)
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping(CartRoute.ADD_TO_CART)
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String foodId = request.getFoodId();
            if(foodId == null || foodId.isEmpty()) {
                apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
                apiResponse.setMessage("FoodId not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
            CartResponse result = cartService.addToCart(request);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Add to cart success");
            apiResponse.setData(result);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCart() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            CartResponse result = cartService.getCart();
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Get cart success");
            apiResponse.setData(result);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @DeleteMapping(CartRoute.CLEAR_CART)
    public ResponseEntity<ApiResponse> clearCart() {
        ApiResponse apiResponse = new ApiResponse();
        try {
            cartService.clearCart();
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Delete cart success");
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @PostMapping(CartRoute.REMOVE_FROM_CART)
    public ResponseEntity<ApiResponse> removeFromCart(@RequestBody  CartRequest request) {
        ApiResponse apiResponse = new ApiResponse();
       try {
           String foodId = request.getFoodId();
           if(foodId == null || foodId.isEmpty()) {
               apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
               apiResponse.setMessage("FoodId not found");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
           }
           CartResponse response = cartService.removeFromCart(request);
           apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
           apiResponse.setMessage("Remove From Cart Success");
           apiResponse.setData(response);
           return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
       }catch (Exception ex) {
           apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
           apiResponse.setMessage(ex.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
       }
    }

    @PostMapping(CartRoute.REMOVE_ITEM_FROM_CART)
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestBody  CartRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String foodId = request.getFoodId();
            if(foodId == null || foodId.isEmpty()) {
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                apiResponse.setMessage("FoodId not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
            CartResponse response = cartService.removeItemFromCart(request);
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Remove Item From Cart Success");
            apiResponse.setData(response);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

}
