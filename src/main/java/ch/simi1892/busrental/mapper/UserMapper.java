package ch.simi1892.busrental.mapper;

import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.AddressDbo;
import ch.simi1892.busrental.entity.UserDbo;

import java.time.LocalDate;

public final class UserMapper {
    private UserMapper() { }

    public static UserDto toDto(UserDbo user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getAddress().getStreet(),
        user.getAddress().getStreetNr(),
        user.getAddress().getZip(),
        user.getAddress().getCity(),
        user.getUserRole()
        );
    }

    public static UserDbo toDbo(UserRegistrationDto dto) {
        UserDbo user = new UserDbo();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setCreateDate(LocalDate.now());

        AddressDbo address = new AddressDbo();
        address.setStreet(dto.street());
        address.setStreetNr(dto.streetNr());
        address.setZip(dto.zip());
        address.setCity(dto.city());
        user.setAddress(address);

        return user;
    }
}
