package fis.prspr.MassageBooking.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Masseuse;
import fis.prspr.MassageBooking.Repository.MasseuseRepository;
import jakarta.annotation.PostConstruct;

@Service
public class MasseuseService {

    @Autowired
    private MasseuseRepository repo; 

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            repo.saveAll(List.of(
                new Masseuse("May", new ArrayList<>()),
                new Masseuse("Nan", new ArrayList<>()),
                new Masseuse("Dang", new ArrayList<>()),
                new Masseuse("Pu", new ArrayList<>()),
                new Masseuse("Atima", new ArrayList<>())
            ));
        }}


    // 2. korak: stranka lahko izbere maserko ali brez preference

    public List<Masseuse> getAllMasseuses(){
        return repo.findAll();
    }





}
