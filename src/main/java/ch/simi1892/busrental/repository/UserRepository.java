package ch.simi1892.busrental.repository;

import ch.simi1892.busrental.entity.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDbo, Long> {
}
