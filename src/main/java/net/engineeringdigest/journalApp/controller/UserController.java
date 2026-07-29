package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDB = userService.findByUsername(username);
        try {
            userInDB.setUsername(!user.getUsername().isEmpty() ? user.getUsername() : userInDB.getUsername());
            userInDB.setPassword(!user.getPassword().isEmpty() ? user.getPassword() : userInDB.getPassword());
            userService.saveNewUser(userInDB);
            return new ResponseEntity<>(userInDB, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<String> greeting(@RequestParam(required = false) String city) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String weatherInfoMessage = "";
        if (city != null && ! city.isBlank()){
            WeatherResponse weatherResponse = null;
            weatherResponse = weatherService.getWeather(city.toLowerCase());
            if (weatherResponse != null)
                weatherInfoMessage = ", today Weather feels like " + weatherResponse.getCurrent().getFeelsLike() + " in " + city;
        }

        return new ResponseEntity<>("Hi " + authentication.getName() + weatherInfoMessage, HttpStatus.OK);
    }

}
