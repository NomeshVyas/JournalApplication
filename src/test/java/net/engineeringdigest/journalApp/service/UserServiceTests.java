package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeAll
    public static void staring(){
        System.out.println("Starting testing...");
    }

    @AfterAll
    public static void end(){
        System.out.println("Ending testing...");
    }

//    @Test
//    @Disabled
    @ParameterizedTest
//    @CsvSource({
//            "ram",
//            "Shyam",
//    })
    @ValueSource(strings = {
            "Shyam2.O",
            "Shyam",
            "Admin"
    })
    public void testFindByUsername(String username) {
        assertNotNull(userRepository.findByUsername(username));
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testCreateNewUser(User user) {
        assertTrue(userService.saveNewUser(user));
    }
}
