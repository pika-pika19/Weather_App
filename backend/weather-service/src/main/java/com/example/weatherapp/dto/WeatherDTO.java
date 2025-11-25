package com.example.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {

    private String city;

    private String country;

    @JsonProperty("temperature_celsius")
    private Double temperatureCelsius;

    @JsonProperty("feels_like_celsius")
    private Double feelsLikeCelsius;

    private Integer humidity;

    private Integer pressure;

    @JsonProperty("wind_speed")
    private Double windSpeed;

    private String description;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("timestamp_utc")
    private Long timestampUtc;
}
