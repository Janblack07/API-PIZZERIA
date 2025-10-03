package com.example.apipizzeria.common.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class ApiPageUtil {
    private ApiPageUtil() {}

    /** Meta sin duplicar el contenido (content vacío). Ideal para usar como "meta". */
    public static <T> ApiPage<T> meta(Page<T> page) {
        return new ApiPage<>(
                List.of(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                toSortString(page.getSort())
        );
    }

    /** Versión que incluye el contenido (por si alguna vez la quieres). */
    public static <T> ApiPage<T> full(Page<T> page) {
        return new ApiPage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                toSortString(page.getSort())
        );
    }

    private static String toSortString(Sort sort) {
        return sort == null || sort.isUnsorted() ? null : sort.toString();
    }
}