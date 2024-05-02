package ch.simi1892.busrental.service;

import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserRegistrationDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setStreet("123 Main St");
        userDto.setStreetNr("4A");
        userDto.setZip(12345);
        userDto.setCity("Anytown");
    }

    @Test
    void registerUser_WhenEmailNotInUse_ShouldCreateUser() {
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(UserDbo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserRegistrationDto result = userService.registerUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        Mockito.verify(userRepository).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailInUse_ShouldThrowException() {
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new UserDbo()));

        assertThrows(EmailAlreadyInUseException.class, () -> userService.registerUser(userDto));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }

    @Test
    void registerUser_WhenEmailIsInvalid_ShouldThrowInvalidEmailException() {
        // Arrange
        userDto.setEmail("invalid");


        // Act & Assert
        assertThrows(InvalidEmailException.class, () -> userService.registerUser(userDto));

        // Further Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserDbo.class));
    }
}