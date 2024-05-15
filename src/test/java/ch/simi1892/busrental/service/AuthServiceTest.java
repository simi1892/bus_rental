package ch.simi1892.busrental.service;

import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_WhenEmailNotInUse_ShouldCreateUser() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                "password",
                "password",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new UserDbo()));
        Mockito.when(userRepository.save(Mockito.any(UserDbo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = authService.registerUser(userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto.email(), result.email());
        Mockito.verify(userRepository).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailInUse_ShouldThrowException() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                "password",
                "password",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenThrow(new EmailAlreadyInUseException(""));

        Assertions.assertThrows(EmailAlreadyInUseException.class, () -> authService.registerUser(userDto));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailIsInvalid_ShouldThrowInvalidEmailException() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "invalid",
                "password",
                "password",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );


        // Act & Assert
        Assertions.assertThrows(InvalidEmailException.class, () -> authService.registerUser(userDto));

        // Further Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                "",
                "password",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );

        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        Assertions.assertEquals("Password must not be empty.", exception.getMessage());

        // Further Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.com",
                null,
                "password",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );


        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        Assertions.assertEquals("Password must not be empty.", exception.getMessage());

        // Further Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenPasswordDoesNotMatch_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto(
                "John",
                "Doe",
                "john@doe.ch",
                "password123",
                "PASSWORD123",
                "123 Main St",
                "4A",
                12345,
                "Anytown"
        );


        // Act & Assert
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        Assertions.assertEquals("Password and confirmation do not match.", exception.getMessage());

        // Further Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

}