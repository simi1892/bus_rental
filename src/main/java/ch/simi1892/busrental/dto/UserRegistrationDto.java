package ch.simi1892.busrental.dto;

public record UserRegistrationDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String passwordConfirmation,
        String street,
        String streetNr,
        int zip,
        String city
) {
}
