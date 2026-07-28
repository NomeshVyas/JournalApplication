package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${WeatherStack.api.key}")
    private String WEATHERSTACK_API_KEY;

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    private final RedisService redisService;

    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService){
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null)
            return weatherResponse;

        String finalApi = appCache.getWeatherApi().replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, WEATHERSTACK_API_KEY);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
        if(response.getBody() != null)
            redisService.set("weather_of_" + city, response.getBody(), 300L);
        return response.getBody();
    }
}
