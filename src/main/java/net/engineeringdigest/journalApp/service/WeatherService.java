package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${WeatherStack.api.key}")
    private String WEATHERSTACK_API_KEY;
    private static final String WEATHERSTACK_API = "http://api.weatherstack.com/current?access_key=ACCESS_KEY&query=CITY";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String finalApi = WEATHERSTACK_API.replace("CITY", city).replace("ACCESS_KEY", WEATHERSTACK_API_KEY);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}
