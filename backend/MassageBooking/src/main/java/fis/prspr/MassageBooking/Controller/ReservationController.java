package fis.prspr.MassageBooking.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fis.prspr.MassageBooking.Entity.ConfirmReservationRequest;
import fis.prspr.MassageBooking.Entity.DeleteReservationRequest;
import fis.prspr.MassageBooking.Entity.EditReservationRequest;
import fis.prspr.MassageBooking.Entity.Massage;
import fis.prspr.MassageBooking.Entity.Masseuse;
import fis.prspr.MassageBooking.Entity.Reservation;
import fis.prspr.MassageBooking.Entity.ReservationDTO;
import fis.prspr.MassageBooking.Service.MassageService;
import fis.prspr.MassageBooking.Service.MasseuseService;
import fis.prspr.MassageBooking.Service.ReservationService;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MassageService massageService; 

    @Autowired
    private MasseuseService masseuseService;

    // customer

    @GetMapping("/my/{customerId}")
    public List<Reservation> getReservationsByCustomerId(@PathVariable Long customerId) {
        return reservationService.getReservationsByCustomerId(customerId);
    }

    @PutMapping("/edit/{id}")
    public String editReservationVerified(@RequestBody EditReservationRequest request) {
        return reservationService.editReservationVerified(request);
    }


    @PostMapping("/delete")
    public String deleteReservation(@RequestBody DeleteReservationRequest request) {
        return reservationService.deleteReservation(request);
    }


    // booking process

    @GetMapping("/massages")
    public List<Massage> getAll(){
        return massageService.getAll();
    }
    @GetMapping("/filter")
    public List<Massage> filterByDurationAndPrice(@RequestParam int duration, @RequestParam double price) {
        return massageService.filterByDurationAndPrice(duration, price);
    }


    @GetMapping("/masseuses")
    public List<Masseuse> getAllMasseuses(){
        return masseuseService.getAllMasseuses();
    }

    @GetMapping("/slots")
    public List<LocalTime> getAvailableSlots(@RequestParam Long masseuseId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam int durationMinutes) {
        return reservationService.getAvailableSlots(masseuseId, date, durationMinutes);
    }

    @GetMapping("/slots/all")
    public Map<Masseuse, List<LocalTime>> getAvailableSlotsAll(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam int durationMinutes) {
        return reservationService.getAvailableSlotsAllMasseuses(date, durationMinutes);
    }

    @PostMapping("/confirm")
    public String confirmReservation(@RequestBody ConfirmReservationRequest request) {
        return reservationService.confirmReservation(
            request.getEmail(),
            request.getPassword(),
            request.getMassageId(),
            request.getMasseuseId(),
            request.getDate(),
            request.getTime()
        );
    }

    // iskanje rezervacij po id
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO dto = reservationService.getReservation(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }


}

