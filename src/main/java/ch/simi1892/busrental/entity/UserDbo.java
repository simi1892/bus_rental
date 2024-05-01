package ch.simi1892.busrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDbo extends BaseDbo {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressDbo address;

    @Column
    private boolean isActive;

    @Column
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        CUSTOMER,
        SUPERUSER,
        ADMIN
    }
}
