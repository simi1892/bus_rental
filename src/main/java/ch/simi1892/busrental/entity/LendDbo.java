package ch.simi1892.busrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @Column(nullable = false)
    @Min(value = 0, message = "The value must be positive")
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
