package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
            userService.saveUser(userInDB);
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

}
