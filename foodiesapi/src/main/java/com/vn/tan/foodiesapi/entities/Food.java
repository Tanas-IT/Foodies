package com.vn.tan.foodiesapi.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "foods") // Đặt tên collection để lưu trong MongoDB
public class Food {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageURL;
}
