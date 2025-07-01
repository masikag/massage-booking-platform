package fis.prspr.MassageBooking.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Customer;
import fis.prspr.MassageBooking.Repository.CustomerRepository;
import jakarta.annotation.PostConstruct;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo; 

    @PostConstruct
    public void init(){
        if (repo.count() == 0) {
            repo.saveAll(List.of(
            new Customer("Ana", "Novak", "040123456", "ana.novak@gmail.com", "123"),
            new Customer("Marko", "Kranjc", "031654321", "marko.kranjc@gmail.com", "markoskace"),
            new Customer("Eva", "Zajc", "051789123", "eva.zajc@gmail.com", "eva1997"),
            new Customer("Luka", "Kovač", "070321654", "luka.kovac@gmail.com", "geslo"),
            new Customer("Tina", "Horvat", "068456789", "tina.horvat@gmail.com", "banana")
            ));
        }
    }



    // registracija stranke + preverjanje ali ze obstaja oz je neregistrirana
    public String registerCustomer(Customer customer) {
        Optional<Customer> existingCustomerOpt = repo.findByEmail(customer.getEmail());

        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();

            // ce stranka ze ima geslo -> je registrirana
            if (existingCustomer.getPassword() != null && !existingCustomer.getPassword().isBlank()) {
                return "Uporabnik s tem e-mailom je že registriran.";
            }

            // ce nima gesla -> obstaja le kot neaktivna stranka -> jo posodobimo 
            existingCustomer.setPassword(customer.getPassword());
            repo.save(existingCustomer);
            return "Registracija uspešna.";
        }

        // ce stranka s tem mailom se ne obstaja -> ustvarimo novo
        repo.save(customer);
        return "Registracija uspešna.";
    }


    // receptionist only!

    public List<Customer> findAll() {
    return repo.findAll();
    }
    public Customer findById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<Customer> searchByName(String query) {
    return repo.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(query, query);
    }

}
