package com.vn.tan.foodiesapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.tan.foodiesapi.controller.routes.FoodRoute;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.FoodRequest;
import com.vn.tan.foodiesapi.io.FoodResponse;
import com.vn.tan.foodiesapi.io.PagedResponse;
import com.vn.tan.foodiesapi.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(FoodRoute.API_BASE_FOOD)
@CrossOrigin("*")
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addFood(@RequestPart("food") String foodString, @RequestPart("file") MultipartFile imageFile) {
        ApiResponse apiResponse = new ApiResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;

        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (JsonProcessingException ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Invalid JSON format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        FoodResponse result = foodService.addFood(request, imageFile);

        apiResponse.setStatusCode(HttpStatus.OK.value());
        apiResponse.setMessage("Add food success");
        apiResponse.setData(result);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllFoods(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "5") int pageSize)
    {
        ApiResponse apiResponse = new ApiResponse();
       try {
           apiResponse.setStatusCode(HttpStatus.OK.value());
           apiResponse.setMessage("Get all foods success");
           PagedResponse<FoodResponse> result = this.foodService.getAllFood(pageIndex, pageSize);
           apiResponse.setData(result);
           return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
       } catch (Exception ex) {
           apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
           apiResponse.setMessage(ex.getMessage());
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getFoodById(@PathVariable("id") String id) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Get food by id success");
            FoodResponse result = this.foodService.getFoodById(id);
            apiResponse.setData(result);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> DeleteFoodById(@PathVariable("id") String id) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Delete food by id success");
            this.foodService.deleteFood(id);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

}
