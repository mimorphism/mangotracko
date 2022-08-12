package com.mimorphism.mangotracko.payload;

import java.util.List;

import lombok.Data;

@Data
public class PagedResponse<T> {

    private List<T> content;
    private long totalRecords;
    private int totalPages;
    private int currentPage;
}
