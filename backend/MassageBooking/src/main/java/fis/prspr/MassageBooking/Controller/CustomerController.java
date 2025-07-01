package fis.prspr.MassageBooking.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fis.prspr.MassageBooking.Entity.Customer;
import fis.prspr.MassageBooking.Service.AuthService;
import fis.prspr.MassageBooking.Service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired 
    private CustomerService customerService; 

    @Autowired
    private AuthService authService; 

    @PostMapping("/register")
    public String registerCustomer(@RequestBody Customer c){
        return customerService.registerCustomer(c);
        
    }

    @GetMapping("/login")
    public Map<String, Object> loginCustomer(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }



    // klice samo receptor
    
    @GetMapping("/all")
    public List<Customer> findAll(){
        return customerService.findAll();
    }

    // klicemo pri my-reservations (customer)
    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id){
        return customerService.findById(id);
    }

    @GetMapping("/receptionist/customers")
    public List<Customer> getCustomers(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) {
            return customerService.findAll();
        } else {
            return customerService.searchByName(search);
        }
}

}
