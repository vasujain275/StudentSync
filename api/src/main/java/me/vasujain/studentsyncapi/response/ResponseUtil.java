package me.vasujain.studentsyncapi.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data){
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .data(data)
                .timestamp(LocalDate.from(LocalDateTime.now()))
                .build();

        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .timestamp(LocalDate.from(LocalDateTime.now()))
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.CREATED)
                .data(data)
                .timestamp(LocalDate.from(LocalDateTime.now()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<Page<T>>> okPage(Page<T> page) {
        ApiResponse<Page<T>> response = ApiResponse.<Page<T>>builder()
                .status(HttpStatus.OK)
                .data(page)
                .pagination(ApiResponse.PaginationMetadata.builder()
                        .totalElements((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(page.getNumber())
                        .pageSize(page.getSize())
                        .build())
                .timestamp(LocalDate.from(LocalDateTime.now()))
                .build();
        return ResponseEntity.ok(response);
    }

}
