package com.example.weatherapp.web;

import com.example.weatherapp.dto.WeatherDTO;
import com.example.weatherapp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*Weather REST Controller
 Exposes RESTful API endpoint for weather search.
 Endpoint: GET /api/v1/weather?city={name}
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "Current weather search by city name")

public class WeatherController {

    private final WeatherService weatherService;


    @GetMapping("/weather")
    @Operation(
            summary = "Get current weather by city name",
            description = "Fetches current weather data from OpenWeatherMap API with caching (TTL: 5 min)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Weather data retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WeatherDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - city parameter missing or blank"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "City not found"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit exceeded"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Upstream service error"
            )
    })

    public ResponseEntity<WeatherDTO> getCurrentWeather(
            @Parameter(description = "City name (e.g., London, Mumbai, Tokyo)", required = true)
            @RequestParam("city") String city) {

        log.info("Received weather request for city: {}", city);

        WeatherDTO weather = weatherService.getWeatherByCity(city);

        log.info("Returning weather data for city: {} ({})", weather.getCity(), weather.getCountry());

        return ResponseEntity.ok(weather);
    }

//    public ResponseEntity<WeatherDTO> getCurrentWeather(@RequestParam("city") String city) {

//      WeatherDTO weather = weatherService.getWeatherByCity(city);
//      return ResponseEntity.ok(weather);
//
//     return ResponseEntity.ok(weatherService.getWeatherByCity(city));
//    }


//      Health check endpoint.

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Weather Service is running");
    }

//     Cache statistics endpoint (for debugging).
    @GetMapping("/cache/stats")
    public ResponseEntity<String> cacheStats() {
        return ResponseEntity.ok(weatherService.getCacheStats());
    }
}
