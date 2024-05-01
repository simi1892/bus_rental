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
    @Column
    private String street;

    @Column
    String streetNr;

    @Column
    private int zip;

    @Column
    private String city;

    @OneToOne(mappedBy = "address")
    private UserDbo user;
}
