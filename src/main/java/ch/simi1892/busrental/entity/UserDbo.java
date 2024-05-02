package ch.simi1892.busrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    @Column(nullable = false, columnDefinition = "int default 0")
    private int discountInPercent;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToMany
    @JoinTable(
            name = "user_lend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lend_id")
    )
    private List<LendDbo> lends;

    public enum UserRole {
        CUSTOMER,
        SUPERUSER,
        ADMIN
    }
}
