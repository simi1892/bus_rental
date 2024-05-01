package ch.simi1892.busrental.repository;

import ch.simi1892.busrental.entity.AddressDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressDbo, Long> {
}
