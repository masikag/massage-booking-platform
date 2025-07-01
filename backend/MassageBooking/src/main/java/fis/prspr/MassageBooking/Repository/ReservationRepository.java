package fis.prspr.MassageBooking.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fis.prspr.MassageBooking.Entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    List<Reservation> findByMasseuseId(Long id);

    List<Reservation> findByMasseuseIdAndStartTimeBetween(Long masseuseId, LocalDateTime start, LocalDateTime end);

    List<Reservation> findByCustomerId(Long id);

}
