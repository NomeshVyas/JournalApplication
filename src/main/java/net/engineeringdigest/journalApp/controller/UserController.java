package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        User isAlreadyExists = userService.findByUsername(user.getUsername());
        if(isAlreadyExists != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username){
        User userInDB = userService.findByUsername(username);
        if (userInDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {

                userInDB.setUsername(!user.getUsername().isEmpty() ? user.getUsername() : userInDB.getUsername());
                userInDB.setPassword(!user.getPassword().isEmpty() ? user.getPassword() : userInDB.getPassword());
                userService.saveUser(userInDB);
                return new ResponseEntity<>(userInDB, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
