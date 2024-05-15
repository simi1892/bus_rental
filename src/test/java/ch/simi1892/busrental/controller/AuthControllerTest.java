package ch.simi1892.busrental.controller;

import ch.simi1892.busrental.config.auth.TokenProvider;
import ch.simi1892.busrental.dto.JwtDto;
import ch.simi1892.busrental.dto.LoginDto;
import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private UserDto userDto;
    private UserRegistrationDto userRegistrationDto;
    private LoginDto loginDto;
    private Authentication authentication;
    private User user;

    @BeforeEach
    public void setUp() {
        userDto = createUserDto();
        userRegistrationDto = createUserRegistrationDto();
        loginDto = createLoginDto();
        user = new User("john.doe@example.com", "password", new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Test
    void testRegisterUser() {
        // Arrange
        Mockito.when(authService.registerUser(userRegistrationDto)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = authController.registerUser(userRegistrationDto);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDto, response.getBody());
    }

    @Test
    void testSignIn() {
        // Arrange
        String token = "dummy-jwt-token";
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(tokenProvider.generateAccessToken(ArgumentMatchers.eq(user))).thenReturn(token);

        // Act
        ResponseEntity<JwtDto> response = authController.signIn(loginDto);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(new JwtDto(token), response.getBody());
    }

    private UserDto createUserDto() {
        return new UserDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "Main St",
                "1234",
                12345,
                "Anytown",
                UserDbo.UserRole.CUSTOMER
        );
    }

    private UserRegistrationDto createUserRegistrationDto() {
        return new UserRegistrationDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password",
                "Password",
                "Main St",
                "1234",
                12345,
                "Anytown"
        );
    }

    private LoginDto createLoginDto() {
        return new LoginDto(
                "john.doe@example.com",
                "password"
        );
    }
}