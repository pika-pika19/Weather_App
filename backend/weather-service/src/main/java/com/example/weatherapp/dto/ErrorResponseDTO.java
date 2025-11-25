package com.example.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorResponseDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Optional field (for rate limit cases)
    private Integer retryAfter;
}
