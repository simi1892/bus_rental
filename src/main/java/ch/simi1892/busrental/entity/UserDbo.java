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

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressDbo address;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        CUSTOMER,
        SUPERUSER,
        ADMIN
    }
}
