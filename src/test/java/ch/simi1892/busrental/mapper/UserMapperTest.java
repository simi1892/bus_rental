package ch.simi1892.busrental.mapper;

import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.AddressDbo;
import ch.simi1892.busrental.entity.UserDbo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    @Test
    void testToDto() {
        // Arrange
        AddressDbo address = new AddressDbo();
        address.setStreet("123 Elm St");
        address.setStreetNr("10A");
        address.setZip(12345);
        address.setCity("Metropolis");

        UserDbo user = new UserDbo();
        user.setFirstName("Clark");
        user.setLastName("Kent");
        user.setEmail("clark.kent@example.com");
        user.setPassword("Password");
        user.setAddress(address);
        user.setUserRole(UserDbo.UserRole.CUSTOMER);

        // Act
        UserDto dto = UserMapper.toDto(user);

        // Assert
        Assertions.assertEquals("Clark", dto.firstName());
        Assertions.assertEquals("Kent", dto.lastName());
        Assertions.assertEquals("clark.kent@example.com", dto.email());
        Assertions.assertEquals("123 Elm St", dto.street());
        Assertions.assertEquals("10A", dto.streetNr());
        Assertions.assertEquals(12345, dto.zip());
        Assertions.assertEquals("Metropolis", dto.city());
        Assertions.assertEquals(UserDbo.UserRole.CUSTOMER, dto.userRole());
    }

    @Test
    void testToDto_whenNull_returnNull() {
        UserDbo user = null;

        // Act
        UserDto dto = UserMapper.toDto(user);

        // Assert
        Assertions.assertNull(dto);
    }

    @Test
    void testToDbo() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto(
                "Bruce",
                "Wayne",
                "bruce.wayne@example.com",
                "Password",
                "1007 Mountain Drive",
                "Gothamstreet",
                "1a",
                10101,
                "Gotham"
        );

        // Act
        UserDbo user = UserMapper.toDbo(dto);

        // Assert
        Assertions.assertEquals(dto.firstName(), user.getFirstName());
        Assertions.assertEquals(dto.lastName(), user.getLastName());
        Assertions.assertEquals(dto.email(), user.getEmail());
        Assertions.assertEquals(dto.password(), user.getPassword());
        Assertions.assertEquals(dto.street(), user.getAddress().getStreet());
        Assertions.assertEquals(dto.streetNr(), user.getAddress().getStreetNr());
        Assertions.assertEquals(dto.zip(), user.getAddress().getZip());
        Assertions.assertEquals(dto.city(), user.getAddress().getCity());
    }
}