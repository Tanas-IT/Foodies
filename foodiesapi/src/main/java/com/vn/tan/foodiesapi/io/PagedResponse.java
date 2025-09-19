package com.vn.tan.foodiesapi.io;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PagedResponse<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
