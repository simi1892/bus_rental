package ch.simi1892.busrental.repository;

import ch.simi1892.busrental.entity.CustomerDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerDbo, Long> {
}
