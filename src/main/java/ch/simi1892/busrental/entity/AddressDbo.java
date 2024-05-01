package ch.simi1892.busrental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AddressDbo extends BaseDbo {
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    String streetNr;

    @Column(nullable = false)
    private int zip;

    @Column(nullable = false)
    private String city;

    @OneToOne(mappedBy = "address")
    private UserDbo user;
}
