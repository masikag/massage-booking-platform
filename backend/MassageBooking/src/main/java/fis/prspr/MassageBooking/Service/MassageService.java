package fis.prspr.MassageBooking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Massage;
import fis.prspr.MassageBooking.Repository.MassageRepository;
import jakarta.annotation.PostConstruct;

@Service
public class MassageService {

    @Autowired
    private MassageRepository repo;

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            repo.saveAll(List.of(
                new Massage("Tradicionalna tajska masaža hrbta", 30, 35.00),
                new Massage("Tajska masaža hrbta z aromatičnimi olji", 30, 38.00),
                new Massage("Tajska masaža celega telesa", 50, 51.00),
                new Massage("Tajska masaža celega telesa z aromatičnimi olji", 50, 59.00),
                new Massage("Tajska masaža celega telesa z aromatično kremo", 50, 59.00),
                new Massage("Tajska masaža celega telesa s toplimi zelišči", 50, 62.00),
                new Massage("Tajska masaža celega telesa z aromatičnimi olji", 90, 78.00),
                new Massage("Kombinacija tajske masaže in masaže z olji", 90, 78.00),

                new Massage("ZEN masaža hrbta", 30, 38.00),
                new Massage("ZEN masaža celega telesa", 50, 59.00),
                new Massage("ZEN masaža celega telesa", 90, 78.00),

                new Massage("Masaža hrbta z vročimi vulkanskimi kamni", 30, 38.00),
                new Massage("Masaža celega telesa z vročimi kamni", 50, 59.00),
                new Massage("Masaža celega telesa z vročimi kamni", 90, 78.00),

                new Massage("Tajska antistresna masaža obraza", 30, 35.00),
                new Massage("Azijska refleksna masaža stopal", 40, 35.00),
                new Massage("Refleksna masaža rok in stopal", 50, 50.00)));
        }}

    // 1. korak (izberite storitev)
    public List<Massage> getAll(){
        return repo.findAll();
    }

    // v prvem koraku lahko stranka filtrira po dolžini masaže in po ceni
    public List<Massage> filterByDurationAndPrice(int duration, double maxPrice) {
        return repo.findByDurationAndPriceLessThanEqual(duration, maxPrice);
    }


}
