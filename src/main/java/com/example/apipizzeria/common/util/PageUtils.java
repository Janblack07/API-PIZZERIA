package com.example.apipizzeria.common.util;


import com.example.apipizzeria.common.api.ApiPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public final class PageUtils {
    private PageUtils(){}

    public static <T> ApiPage<T> toApiPage(Page<T> page) {
        String sort = page.getSort() == null || page.getSort().isUnsorted()
                ? null
                : page.getSort().stream()
                .map(o -> o.getProperty() + "," + (o.getDirection() == Sort.Direction.ASC ? "asc" : "desc"))
                .reduce((a,b) -> a + ";" + b).orElse(null);

        return new ApiPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                sort
        );
    }
}