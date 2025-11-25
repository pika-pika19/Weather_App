package com.example.weatherapp.client;

import com.example.weatherapp.dto.OpenWeatherResponse;
import com.example.weatherapp.exception.CityNotFoundException;
import com.example.weatherapp.exception.UpstreamException;
import com.example.weatherapp.exception.UpstreamRateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;

@Slf4j
@Component
public class WeatherClient {

    private final WebClient webClient;
    private final String apiKey;
    private final int maxRetries;
    private final Duration initialRetryDelay;

    public WeatherClient(
            @Value("${openweathermap.base-url}") String baseUrl,
            @Value("${openweathermap.api-key}") String apiKey,
            @Value("${openweathermap.timeout.connect}") Duration connectTimeout,
            @Value("${openweathermap.timeout.read}") Duration readTimeout,
            @Value("${openweathermap.retry.max-attempts}") int maxRetries,
            @Value("${openweathermap.retry.initial-delay}") Duration initialRetryDelay
    ) {
        this.apiKey = apiKey;
        this.maxRetries = maxRetries;
        this.initialRetryDelay = initialRetryDelay;

        // Configure Netty timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout.toMillis())
                .responseTimeout(readTimeout);

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        log.info("WeatherClient initialized with baseUrl: {}", baseUrl);
    }

   //  Fetch current weather for a city

    public OpenWeatherResponse getCurrentWeather(String city) {

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City name must not be empty");
        }

        log.debug("Fetching weather for city: {}", city);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")  // Celsius
                        .build())
                .retrieve()

                // --- Handle HTTP statuses ---
                .onStatus(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        clientResponse -> Mono.error(new CityNotFoundException(city))
                )
                .onStatus(
                        status -> status.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.error(new UpstreamException("Invalid API credentials"))
                )
                .onStatus(
                        status -> status.equals(HttpStatus.TOO_MANY_REQUESTS),
                        clientResponse -> {
                            String retryAfterHeader = clientResponse.headers()
                                    .asHttpHeaders()
                                    .getFirst("Retry-After");

                            Integer retryAfter = null;
                            try {
                                if (retryAfterHeader != null && !retryAfterHeader.isBlank()) {
                                    retryAfter = Integer.parseInt(retryAfterHeader.trim());
                                }
                            } catch (NumberFormatException ignored) {}

                            return Mono.error(new UpstreamRateLimitException("Rate limit exceeded", retryAfter));
                        }
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new UpstreamException("Vendor service error: " + clientResponse.statusCode()))
                )

                // --- Deserialize JSON into our DTO ---
                .bodyToMono(OpenWeatherResponse.class)

                // --- Retry ONLY on 429 Too Many Requests ---
                .retryWhen(Retry.backoff(maxRetries, initialRetryDelay)
                        .filter(e -> e instanceof UpstreamRateLimitException)
                        .doBeforeRetry(retrySignal -> log.warn(
                                "Retry attempt #{} due to rate limit (429)",
                                retrySignal.totalRetries() + 1))
                )

                // --- Logging ---
                .doOnSuccess(resp ->
                        log.debug("Successfully fetched weather for city: {}", city))
                .doOnError(err ->
                        log.error("Error fetching weather for {}: {}", city, err.getMessage()))

                // --- Convert reactive chain to blocking call ---
                .onErrorMap(WebClientResponseException.class,
                        ex -> new UpstreamException("Vendor response error: " + ex.getMessage(), ex))

                .block();
    }
}
