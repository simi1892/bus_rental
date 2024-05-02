package ch.simi1892.busrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BusDbo extends BaseDbo {
    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    @Min(value = 1900, message = "The value must be greater than 1900")
    private int builtYear;

    @Column(nullable = false)
    private int purchaseYear;

    @Column(nullable = false)
    @Min(value = 1, message = "The value must be greater than 1")
    private int capacity;

    @Column(nullable = false)
    private LocalDate lastServiceDate;

    @Column(nullable = false)
    @Min(value = 0, message = "The value must be positive")
    private Double AmountKmWhenBought;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PictureDbo> pictures;

    @ManyToMany
    @JoinTable(
            name = "bus_lend",
            joinColumns = @JoinColumn(name = "bus_id"),
            inverseJoinColumns = @JoinColumn(name = "lend_id")
    )
    private List<LendDbo> lends;
}
