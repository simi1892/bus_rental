package ch.simi1892.busrental.mapper;

import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.AddressDbo;
import ch.simi1892.busrental.entity.UserDbo;

import java.time.LocalDate;

public final class UserMapper {
    private UserMapper() { }

    public static UserRegistrationDto toDto(UserDbo user) {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setStreet(user.getAddress().getStreet());
        dto.setStreetNr(user.getAddress().getStreetNr());
        dto.setZip(user.getAddress().getZip());
        dto.setCity(user.getAddress().getCity());
        return dto;
    }

    public static UserDbo toDbo(UserRegistrationDto dto) {
        UserDbo user = new UserDbo();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setCreateDate(LocalDate.now());

        AddressDbo address = new AddressDbo();
        address.setStreet(dto.getStreet());
        address.setStreetNr(dto.getStreetNr());
        address.setZip(dto.getZip());
        address.setCity(dto.getCity());
        user.setAddress(address);

        return user;
    }
}
