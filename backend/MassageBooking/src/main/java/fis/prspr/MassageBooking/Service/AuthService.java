package fis.prspr.MassageBooking.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Customer;
import fis.prspr.MassageBooking.Entity.Receptionist;
import fis.prspr.MassageBooking.Repository.CustomerRepository;
import fis.prspr.MassageBooking.Repository.ReceptionistRepository;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepo; 

    @Autowired
    private ReceptionistRepository receptionistRepo; 



    public Customer authenticateCustomer(String email, String password) {
        Optional <Customer> stranka = customerRepo.findByEmail(email);
        if(stranka.isPresent() && stranka.get().getPassword().equals(password)){
            return stranka.get();
        }else{
            return null;
        }
    }

    // prijava stranke

    public Map<String, Object> login(String email, String password) {
        Customer customer = authenticateCustomer(email, password);
        Map<String, Object> response = new HashMap<>();

        if (customer != null) {
            response.put("message", "Prijava uspešna");
            response.put("customerId", customer.getId());
            response.put("name", customer.getName());
            response.put("surname", customer.getSurname());
            response.put("email", customer.getEmail());
        } else {
            response.put("message", "Prijava neuspešna");
        }

        return response;
    }


    // prijava receptorja

    public String authenticateReceptionist(String username, String password) {
        Receptionist receptionist = receptionistRepo.findByUsername(username);
        if (receptionist != null && receptionist.getPassword().equals(password)) {
            return "Prijava uspešna!";
        }
        return "Prijava neuspešna.";
    }




}
