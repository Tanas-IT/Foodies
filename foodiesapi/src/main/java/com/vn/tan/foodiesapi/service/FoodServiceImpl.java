package com.vn.tan.foodiesapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vn.tan.foodiesapi.entities.Food;
import com.vn.tan.foodiesapi.io.FoodRequest;
import com.vn.tan.foodiesapi.io.FoodResponse;
import com.vn.tan.foodiesapi.io.PagedResponse;
import com.vn.tan.foodiesapi.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {


    private final Cloudinary cloudinary;
    private final FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(Cloudinary cloudinary, FoodRepository foodRepository) {
        this.cloudinary = cloudinary;
        this.foodRepository = foodRepository;
    }

    @Override
    public String uploadFile(MultipartFile file){
       try {
           Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
           if(!uploadResult.isEmpty()) {
               return uploadResult.get("secure_url").toString();
           }
           else {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
           }
       } catch (IOException ex) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while uploading the file");
       }
    }

    @Override
    public FoodResponse addFood(FoodRequest addFood, MultipartFile imageFile) {
        Food newFood = convertoEntity(addFood);
        String imageURL = uploadFile(imageFile);
        newFood.setImageURL(imageURL);
        newFood = this.foodRepository.save(newFood);

        return convertToResponse(newFood);
    }

    @Override
    public PagedResponse<FoodResponse> getAllFood(int pageIndex, int pageSize) {
        Page<Food> listFood = this.foodRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));
        PagedResponse<FoodResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setCurrentPage(pageIndex);
        pagedResponse.setTotalPages(listFood.getTotalPages());
        pagedResponse.setTotalItems(listFood.getTotalElements());

        List<Food> databaseEntries = listFood.stream().toList();

        pagedResponse.setData(databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList()));
        return pagedResponse;
    }

    @Override
    public FoodResponse getFoodById(String id) {
        Food food = this.foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Food not found for the id: " + id));
        return convertToResponse(food);
    }

    @Override
    public boolean deleteFile(String url) {
        try {
            String getPublicId = extractPublicId(url);
            Map result = this.cloudinary.uploader().destroy(getPublicId, ObjectUtils.asMap(
                    "invalidate", true,"resource_type", "image"));
            return true;

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while deleting the file");
        }
    }

    @Override
    public void deleteFood(String id) {
        Food checkFoodExist = this.foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Food does not exist"));
        boolean isFileDeleted = deleteFile(checkFoodExist.getImageURL());
        if(isFileDeleted) {
            this.foodRepository.delete(checkFoodExist);
        }
    }

    private String extractPublicId(String url) {
        // Bỏ query string (nếu có)
        String cleanUrl = url.split("\\?")[0];

        // Lấy phần path sau "/upload/"
        int index = cleanUrl.indexOf("/upload/");
        if (index == -1) {
            throw new IllegalArgumentException("URL does not valid");
        }

        String path = cleanUrl.substring(index + 8); // sau "/upload/"

        // Nếu có version (bắt đầu bằng "v" + số), thì bỏ nó đi
        String[] parts = path.split("/");
        if (parts.length > 1 && parts[0].matches("^v\\d+$")) {
            // Bỏ phần version
            path = String.join("/", Arrays.copyOfRange(parts, 1, parts.length));
        }

        // Bỏ extension (.jpg, .png, ...)
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex != -1) {
            path = path.substring(0, dotIndex);
        }

        return path;
    }

    private Food convertoEntity(FoodRequest request) {
        Food food = new Food();
        food.setName(request.getName());
        food.setCategory(request.getCategory());
        food.setPrice(request.getPrice());
        food.setDescription(request.getDescription());
        return food;
    }

    private FoodResponse convertToResponse(Food food) {
        FoodResponse foodResponse = new FoodResponse();

        foodResponse.setId(food.getId());
        foodResponse.setName(food.getName());
        foodResponse.setPrice(food.getPrice());
        foodResponse.setDescription(food.getDescription());
        foodResponse.setCategory(food.getCategory());
        foodResponse.setImageURL(food.getImageURL());

        return foodResponse;
    }
}
