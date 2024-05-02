package ch.simi1892.busrental.repository;

import ch.simi1892.busrental.entity.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDbo, Long> {
    Optional<UserDbo> findByEmail(String email);
}
