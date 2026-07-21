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

    public WeatherService(RestTemplate restTemplate, AppCache appCache){
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }

    public WeatherResponse getWeather(String city) {
        String finalApi = appCache.getWeatherApi().replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, WEATHERSTACK_API_KEY);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}
