package ch.simi1892.busrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class LendDbo extends BaseDbo {
    @ManyToMany(mappedBy = "lends")
    private List<BusDbo> buses;

    @ManyToMany(mappedBy = "lends")
    private List<UserDbo> users;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    // TODO: add @Min(value = 0L, message = "The value must be positive") to several places
    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    private LendStatus lendStatus;

    public enum LendStatus {
        REQUESTED,
        CONFIRMED,
        ENDED,
        COMPLETED,
        CANCELED
    }
}
