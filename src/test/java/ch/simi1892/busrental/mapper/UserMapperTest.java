package ch.simi1892.busrental.mapper;

import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.AddressDbo;
import ch.simi1892.busrental.entity.UserDbo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        Assertions.assertEquals("Clark", dto.getFirstName());
        Assertions.assertEquals("Kent", dto.getLastName());
        Assertions.assertEquals("clark.kent@example.com", dto.getEmail());
        Assertions.assertEquals("123 Elm St", dto.getStreet());
        Assertions.assertEquals("10A", dto.getStreetNr());
        Assertions.assertEquals(12345, dto.getZip());
        Assertions.assertEquals("Metropolis", dto.getCity());
        Assertions.assertEquals(UserDbo.UserRole.CUSTOMER, dto.getUserRole());
    }

    @Test
    void testToDbo() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstName("Bruce");
        dto.setLastName("Wayne");
        dto.setEmail("bruce.wayne@example.com");
        dto.setPassword("Password");
        dto.setStreet("1007 Mountain Drive");
        dto.setStreetNr("1");
        dto.setZip(10101);
        dto.setCity("Gotham");

        // Act
        UserDbo user = UserMapper.toDbo(dto);

        // Assert
        Assertions.assertEquals(dto.getFirstName(), user.getFirstName());
        Assertions.assertEquals(dto.getLastName(), user.getLastName());
        Assertions.assertEquals(dto.getEmail(), user.getEmail());
        Assertions.assertEquals(dto.getPassword(), user.getPassword());
        Assertions.assertEquals(dto.getStreet(), user.getAddress().getStreet());
        Assertions.assertEquals(dto.getStreetNr(), user.getAddress().getStreetNr());
        Assertions.assertEquals(dto.getZip(), user.getAddress().getZip());
        Assertions.assertEquals(dto.getCity(), user.getAddress().getCity());
    }
}