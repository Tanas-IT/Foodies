package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.entities.Food;
import com.vn.tan.foodiesapi.io.FoodRequest;
import com.vn.tan.foodiesapi.io.FoodResponse;
import com.vn.tan.foodiesapi.io.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest addFood, MultipartFile imageFile);

    PagedResponse<FoodResponse> getAllFood(int pageIndex, int pageSize);

    FoodResponse getFoodById(String id);

    boolean deleteFile (String url);

    void deleteFood(String id);
}
