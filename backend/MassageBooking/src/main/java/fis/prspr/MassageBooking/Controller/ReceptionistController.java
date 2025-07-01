package fis.prspr.MassageBooking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import fis.prspr.MassageBooking.Entity.Customer;
import fis.prspr.MassageBooking.Entity.NewReservationRequest;
import fis.prspr.MassageBooking.Entity.Receptionist;
import fis.prspr.MassageBooking.Entity.Reservation;
import fis.prspr.MassageBooking.Entity.ReservationDTO;
import fis.prspr.MassageBooking.Service.AuthService;
import fis.prspr.MassageBooking.Service.CustomerService;
import fis.prspr.MassageBooking.Service.ReceptionistService;
import fis.prspr.MassageBooking.Service.ReservationService;

@RestController
@RequestMapping("/api/receptionist")
public class ReceptionistController {

    @Autowired
    private ReceptionistService receptionistService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public String login(@RequestBody Receptionist r) {
        return authService.authenticateReceptionist(r.getUsername(), r.getPassword());
    }

    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return reservationService.getFullReservation();
    }

    @GetMapping("/reservations/masseuse/{id}")
    public List<Reservation> getReservationsByMasseuse(@PathVariable Long id) {
        return reservationService.getReservationsByMasseuse(id);
    }

    @PostMapping("/addreservation")
    public String addReservation(@RequestBody NewReservationRequest request) {
        return reservationService.addReservation(request);
    }


    @PutMapping("/editreservation/{id}")
    public String editReservation(@RequestBody ReservationDTO dto) {
        return reservationService.editReservation(dto);
    }

    @DeleteMapping("/deletereservation/{id}")
    public String deleteReservation(@PathVariable Long id) {
        return reservationService.deleteReservation(id);
    }

    // testing purposes only
    @GetMapping("/all")
    public List<Receptionist> getAllReceptionists() {
        return receptionistService.getAll();
    }

    @GetMapping("/allcustomers")
    public List<Customer> getAllCustomers(){
        return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }
}
