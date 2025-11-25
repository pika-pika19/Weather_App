package com.example.weatherapp.exception;

import lombok.Getter;

@Getter
public class UpstreamRateLimitException extends RuntimeException {

    private final Integer retryAfter;

    public UpstreamRateLimitException(String message, Integer retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }

    public UpstreamRateLimitException(String message) {
        this(message, null);
    }
}
