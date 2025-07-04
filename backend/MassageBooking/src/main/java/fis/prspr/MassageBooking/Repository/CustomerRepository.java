package fis.prspr.MassageBooking.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fis.prspr.MassageBooking.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional <Customer> findByEmail(String email);

    List<Customer> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String query, String query2);

}
