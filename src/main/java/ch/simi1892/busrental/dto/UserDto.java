package ch.simi1892.busrental.dto;

import ch.simi1892.busrental.entity.UserDbo;

public record UserDto(
        String firstName,
        String lastName,
        String email,
        String street,
        String streetNr,
        int zip,
        String city,
        UserDbo.UserRole userRole
) {
}
