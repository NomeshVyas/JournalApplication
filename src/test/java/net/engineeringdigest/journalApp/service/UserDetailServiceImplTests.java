package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
public class UserDetailServiceImplTests {
    @InjectMocks
    private UserDetailServiceImpl userDetailServiceImpl;

    @Mock
    private UserRepository userRepository;

    /* initMocks is deprecated so we should use @ExtendWith(MockitoExtension.class)
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    */

    @Test
    void loadUserByUsernameTest(){
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("Shyam").password("123").roles(new ArrayList<>()).build());
        UserDetails user = userDetailServiceImpl.loadUserByUsername("Shyam");
        Assertions.assertNotNull(user);
    }
}