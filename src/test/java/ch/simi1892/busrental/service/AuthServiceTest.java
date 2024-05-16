package ch.simi1892.busrental.service;

import ch.simi1892.busrental.config.auth.TokenProvider;
import ch.simi1892.busrental.dto.LoginDto;
import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.AddressDbo;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.mapper.UserMapper;
import ch.simi1892.busrental.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private UserRegistrationDto userRegistrationDto;
    private UserDbo userDbo;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                "password",
                "password",
                "Main St",
                "1234",
                12345,
                "Anytown"
        );

        userDbo = new UserDbo();
        userDbo.setFirstName("John");
        userDbo.setLastName("Doe");
        userDbo.setEmail("john@doe.com");
        userDbo.setPassword("password");
        userDbo.setUserRole(UserDbo.UserRole.CUSTOMER);

        AddressDbo address = new AddressDbo();
        address.setStreet("Main St");
        address.setStreetNr("1234");
        address.setZip(12345);
        address.setCity("Anytown");
        userDbo.setAddress(address);

        loginDto = new LoginDto(
                "john@doe.com",
                "password"
        );
    }

    @Test
    void registerUser_WhenEmailNotInUse_ShouldCreateUser() {
        // Arrange
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(UserDbo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto result = authService.registerUser(userRegistrationDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userRegistrationDto.email(), result.email());
        Mockito.verify(userRepository).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailInUse_ShouldThrowException() {
        // Arrange
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(userDbo));

        // Act & Assert
        Assertions.assertThrows(EmailAlreadyInUseException.class, () -> authService.registerUser(userRegistrationDto));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailIsInvalid_ShouldThrowInvalidEmailException() {
        // Arrange
        UserRegistrationDto invalidEmailDto = new UserRegistrationDto(
                "John",
                "Doe",
                "invalid",
                "password",
                "password",
                "Main St",
                "1234",
                12345,
                "Anytown"
        );

        // Act & Assert
        Assertions.assertThrows(InvalidEmailException.class, () -> authService.registerUser(invalidEmailDto));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto emptyPasswordDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                "",
                "",
                "Main St",
                "1234",
                12345,
                "Anytown"
        );

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(emptyPasswordDto));
        Assertions.assertEquals("Password must not be empty.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto nullPasswordDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                null,
                null,
                "Main St",
                "1234",
                12345,
                "Anytown"
        );

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(nullPasswordDto));
        Assertions.assertEquals("Password must not be empty.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordDoesNotMatch_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto nonMatchingPasswordsDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.ch",
                "password123",
                "PASSWORD123",
                "Main St",
                "1234",
                12345,
                "Anytown"
        );

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(nonMatchingPasswordsDto));
        Assertions.assertEquals("Password and confirmation do not match.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnToken() {
        // Arrange
        String token = "dummy-jwt-token";
        UserDbo userDbo = new UserDbo();
        userDbo.setEmail("john@doe.com");
        userDbo.setPassword("password");
        userDbo.setUserRole(UserDbo.UserRole.CUSTOMER);

        AddressDbo address = new AddressDbo();
        address.setStreet("Main St");
        address.setStreetNr("1234");
        address.setZip(12345);
        address.setCity("Anytown");
        userDbo.setAddress(address);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDbo, null, new ArrayList<>());

        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(tokenProvider.generateAccessToken(ArgumentMatchers.any(User.class))).thenReturn(token);

        // Act
        String result = authService.login(loginDto);

        // Assert
        Assertions.assertEquals(token, result);
    }

    @Test
    void login_WhenCredentialsAreInvalid_ShouldThrowIllegalArgumentException() {
        // Arrange
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.login(loginDto));
        Assertions.assertEquals("Invalid credentials", exception.getMessage());
    }
}
