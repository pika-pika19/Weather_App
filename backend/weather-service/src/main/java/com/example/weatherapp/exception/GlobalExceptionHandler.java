package com.example.weatherapp.exception;

import com.example.weatherapp.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

/**
 * Global Exception Handler - Centralized exception handling
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCityNotFound(
            CityNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(UpstreamRateLimitException.class)
    public ResponseEntity<ErrorResponseDTO> handleRateLimit(
            UpstreamRateLimitException ex, WebRequest request) {

        return buildErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS,
                ex.getMessage(),
                request,
                ex.getRetryAfter() // Add retryAfter from exception
        );
    }

    @ExceptionHandler(UpstreamException.class)
    public ResponseEntity<ErrorResponseDTO> handleUpstreamError(
            UpstreamException ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_GATEWAY,
                "Upstream service unavailable: " + ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(
            IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericError(
            Exception ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request,
                null
        );
    }

    // ---- Utility Method ----

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(
            HttpStatus status,
            String message,
            WebRequest request,
            Integer retryAfterOptional
    ) {
        ErrorResponseDTO body = ErrorResponseDTO.builder()
                .timestamp(Instant.now().toString())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .retryAfter(retryAfterOptional)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
