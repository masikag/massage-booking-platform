package fis.prspr.MassageBooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fis.prspr.MassageBooking.Entity.Receptionist;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Long>{

    Receptionist findByUsername(String username); 


}
