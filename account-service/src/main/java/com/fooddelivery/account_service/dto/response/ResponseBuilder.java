package com.fooddelivery.account_service.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseBuilder {

    private ResponseBuilder() {
        // Prevent instantiation
    }

    public static <T> ResponseEntity<StandardResponse<List<T>>> successPaginated(
            String message,
            Page<T> page
    ) {
        PaginationMetaData metaData = PaginationMetaData.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .first(page.isFirst())
                .last(page.isLast())
                .build();

        StandardResponse<List<T>> response = new StandardResponse<>(
                true,
                message,
                HttpStatus.OK.value(),
                page.getContent(),
                metaData
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static <T> ResponseEntity<StandardResponse<T>> success(
            String message,
            T data,
            HttpStatus status
    ) {
        StandardResponse<T> response = new StandardResponse<>(
                true,
                message,
                status.value(),
                data,
                null
        );
        return ResponseEntity.status(status).body(response);
    }
}
