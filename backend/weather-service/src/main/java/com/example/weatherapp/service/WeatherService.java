package com.example.weatherapp.service;

import com.example.weatherapp.client.WeatherClient;
import com.example.weatherapp.dto.OpenWeatherResponse;
import com.example.weatherapp.dto.WeatherDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WeatherService {

    private final WeatherClient weatherClient;
    private final Cache<String, WeatherDTO> cache;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;

        // Caffeine cache setup
        this.cache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(300, TimeUnit.SECONDS)
                .recordStats()
                .build();

        log.info("WeatherService initialized with cache (max: 500, TTL: 300s)");
    }

    public WeatherDTO getWeatherByCity(String city) {

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City parameter is required and cannot be blank");
        }

        String cacheKey = city.trim().toLowerCase();
        log.debug("Fetching weather for city: {} (cacheKey={})", city, cacheKey);

        WeatherDTO dto = cache.get(cacheKey, key -> {
            log.info("Cache MISS for city: {} - calling vendor API", city);
            OpenWeatherResponse response = weatherClient.getCurrentWeather(city);
            return mapToDTO(response);
        });

        return dto;

//        return cache.get(cacheKey, key -> mapToDTO(weatherClient.getCurrentWeather(city)));

    }

    private WeatherDTO mapToDTO(OpenWeatherResponse response) {

        String iconCode = (response.getWeather() != null && !response.getWeather().isEmpty())
                ? response.getWeather().get(0).getIcon()
                : "01d"; //"01d" (clear day) icon

        String description = (response.getWeather() != null && !response.getWeather().isEmpty())
                ? response.getWeather().get(0).getDescription()
                : "No description available";

        return WeatherDTO.builder()
                .city(response.getName())
                .country(response.getSys() != null ? response.getSys().getCountry() : "N/A")
                .temperatureCelsius(response.getMain() != null ? response.getMain().getTemp() : null)
                .feelsLikeCelsius(response.getMain() != null ? response.getMain().getFeelsLike() : null)
                .humidity(response.getMain() != null ? response.getMain().getHumidity() : null)
                .pressure(response.getMain() != null ? response.getMain().getPressure() : null)
                .windSpeed(response.getWind() != null ? response.getWind().getSpeed() : null)
                .description(description)
                .iconUrl("http://openweathermap.org/img/wn/" + iconCode + "@2x.png")
//              .timestampUtc(response.getDt() != null ? Instant.ofEpochSecond(response.getDt()) : null )
                .timestampUtc(response.getDt())
                .build();
    }

    public String getCacheStats() {
//      var stats = cache.stats();
        CacheStats stats = cache.stats();
        return String.format(
                "Cache Stats - Hits: %d, Misses: %d, Hit Rate: %.2f%%",
                stats.hitCount(), stats.missCount(), stats.hitRate() * 100
        );
    }
}
