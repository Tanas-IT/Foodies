package com.vn.tan.foodiesapi.repository;

import com.vn.tan.foodiesapi.entities.Food;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<Food, String> {

}
