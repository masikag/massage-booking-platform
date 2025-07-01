package fis.prspr.MassageBooking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Receptionist;
import fis.prspr.MassageBooking.Repository.ReceptionistRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ReceptionistService {

    @Autowired
    private ReceptionistRepository repo; 

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            repo.saveAll(List.of(
                new Receptionist("receptor1", "admin123"),
                new Receptionist("receptor2", "password")
            ));
        }
    }

    // za testing puposes only!
    public List<Receptionist> getAll(){
        return repo.findAll();
    }

}
