package ch.simi1892.busrental.dto;


import ch.simi1892.busrental.entity.UserDbo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String streetNr;
    private int zip;
    private String city;
}
