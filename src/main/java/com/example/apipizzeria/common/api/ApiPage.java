package com.example.apipizzeria.common.api;

import java.util.List;

public class ApiPage<T> {
    private List<T> content;
    private int page;               // 0-based
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private String sort;            // opcional (ej: "name,asc")

    public ApiPage() {}

    public ApiPage(List<T> content, int page, int size, long totalElements, int totalPages, boolean hasNext, boolean hasPrevious, String sort) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.sort = sort;
    }

    // getters
    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isHasNext() { return hasNext; }
    public boolean isHasPrevious() { return hasPrevious; }
    public String getSort() { return sort; }
}
