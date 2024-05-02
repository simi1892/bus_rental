package ch.simi1892.busrental.controller;

import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRegistrationDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = createUserRegistrationDto();
    }

    @Test
    void testRegisterUser() {
        // arrange
        Mockito.when(userService.registerUser(userDto)).thenReturn(userDto);

        // act
        ResponseEntity<UserRegistrationDto> response = userController.registerUser(userDto);

        // assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDto, response.getBody());
    }

    private UserRegistrationDto createUserRegistrationDto() {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setStreet("Main St");
        user.setStreetNr("1234");
        user.setZip(12345);
        user.setCity("Anytown");
        return user;
    }
}