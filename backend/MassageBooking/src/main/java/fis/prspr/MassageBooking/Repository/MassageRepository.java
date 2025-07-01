package fis.prspr.MassageBooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fis.prspr.MassageBooking.Entity.Massage;

public interface MassageRepository extends JpaRepository<Massage, Long>  {
    List<Massage> findByDurationAndPriceLessThanEqual(int duration, double price);
}
