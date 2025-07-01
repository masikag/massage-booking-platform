package fis.prspr.MassageBooking.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fis.prspr.MassageBooking.Entity.Customer;
import fis.prspr.MassageBooking.Entity.DeleteReservationRequest;
import fis.prspr.MassageBooking.Entity.EditReservationRequest;
import fis.prspr.MassageBooking.Entity.Massage;
import fis.prspr.MassageBooking.Entity.Masseuse;
import fis.prspr.MassageBooking.Entity.NewReservationRequest;
import fis.prspr.MassageBooking.Entity.Reservation;
import fis.prspr.MassageBooking.Entity.ReservationDTO;
import fis.prspr.MassageBooking.Repository.CustomerRepository;
import fis.prspr.MassageBooking.Repository.MassageRepository;
import fis.prspr.MassageBooking.Repository.MasseuseRepository;
import fis.prspr.MassageBooking.Repository.ReservationRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repo; 

    @Autowired
    private CustomerRepository customerRepo; 

    @Autowired
    private MasseuseRepository masseuseRepo; 

    @Autowired
    private MassageRepository massageRepo; 

    @Autowired
    private AuthService authService; 



    // metoda za dinamicno dodajanje rezervacij

    @PostConstruct
    public void init() {
        if (repo.count() > 0) {
            System.out.println("Rezervacije že obstajajo: testni podatki ne bodo ponovno dodani.");
            return;
        }

        List<Customer> customers = customerRepo.findAll();
        List<Masseuse> masseuse = masseuseRepo.findAll();
        List<Massage> massage = massageRepo.findAll();

        if (customers.size() < 5 || masseuse.size() < 5 || massage.size() < 5) {
        System.out.println("Ni dovolj testnih podatkov za dodajanje rezervacij.");
        return;
        }

        for (int i = 0; i < 3; i++) {
            LocalDate date = LocalDate.now().plusDays(i); // vsak dan posebi
            for (int j = 0; j < 5; j++) {
                LocalDateTime start;
                if (massage.get(j).getDuration() < 51) {
                    start = date.atTime(10 + j, 0); // za 30 in 50 minutne masaze
                } else {
                    start = date.atTime(10 + j + 1, 0); // za 90 minutne 
                }

                LocalDateTime end = start.plusMinutes(massage.get(j).getDuration());
                Reservation reservation = new Reservation(start, end, customers.get(j), massage.get(j), masseuse.get(j));
                repo.save(reservation);
            }
        }

        System.out.println("Testne rezervacije uspešno dodane.");
    }


    // booking process
    
    // izpis terminov za poljubno maserko
    public List<LocalTime> getAvailableSlots(Long masseuseId, LocalDate date, int durationMinutes) {
        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(7))) {
            throw new IllegalArgumentException("Rezervacija je možna le za naslednjih 7 dni.");
        }

        // dolocimo kdaj se zacne in konca dan, nato poiscemo vse rezervacije na ta dan za izbrano maserko
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Reservation> reservations = repo.findByMasseuseIdAndStartTimeBetween(masseuseId, startOfDay, endOfDay);

        List<LocalTime> availableSlots = new ArrayList<>();

        // dolocimo odpiralni cas
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(20, 0).minusMinutes(durationMinutes);

        while (!start.isAfter(end)) {
            LocalDateTime slotStart = date.atTime(start);
            LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
            boolean isAvailable = true;

            // start1 < end2 && start2 < end1
            for (Reservation r : reservations) {
                if (r.getStartTime().isBefore(slotEnd) && slotStart.isBefore(r.getEndTime())) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) availableSlots.add(start);
            start = start.plusHours(1); // termini na polno uro
        }

        return availableSlots;
    }



    // izpis terminov brez preferenc po maserki 

    public Map<Masseuse, List<LocalTime>> getAvailableSlotsAllMasseuses(LocalDate date, int durationMinutes) {
        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(7))) {
            throw new IllegalArgumentException("Rezervacija je možna le za naslednjih 7 dni.");
        }

        List<Masseuse> masseuses = masseuseRepo.findAll();
        Map<Masseuse, List<LocalTime>> result = new HashMap<>();

        for (Masseuse m : masseuses) {
            List<LocalTime> slots = getAvailableSlots(m.getId(), date, durationMinutes);
            if (!slots.isEmpty()) result.put(m, slots);
        }

        return result;
    }

    // koncna potrditev rezervacije

    public String confirmReservation(String email, String password, Long massageId, Long masseuseId, LocalDate date, LocalTime time){
        Customer customer = authService.authenticateCustomer(email, password);
        if(customer == null){
            return "Napaka: napačni podatki";
        }

        Masseuse masseuse = masseuseRepo.findById(masseuseId).orElse(null);
        Massage massage = massageRepo.findById(massageId).orElse(null);

        if(masseuse == null || massage == null){
            return "Napaka: Neveljavna masaža ali maserka.";
        }

        LocalDateTime start = date.atTime(time);
        LocalDateTime end = start.plusMinutes(massage.getDuration());

        List<Reservation> reservations = repo.findAll();

        for(Reservation r : reservations){
            if(r.getStartTime().isAfter(end) && start.isAfter(r.getEndTime())){
                return "Termin ni več na voljo";
            }
        }

        Reservation reservation = new Reservation(start, end, customer, massage, masseuse);
        repo.save(reservation);
        return "rezervacija uspešno potrjena";
    }


    // customer 

    public List<Reservation> getReservationsByCustomerId(Long customerId) {
        return repo.findByCustomerId(customerId);
    }

    public String editReservationVerified(EditReservationRequest request) {

        Reservation reservation = repo.findById(request.getReservationId()).orElse(null);
        if (reservation == null) {
            return "Rezervacija ne obstaja.";
        }

        Customer customer = customerRepo.findById(request.getCustomerId()).orElse(null);
        if (customer == null) {
            return "Stranka ne obstaja.";
        }

        Massage massage = massageRepo.findById(request.getMassageId()).orElse(null);
        if (massage == null) {
            return "Masaža ne obstaja.";
        }

        Masseuse masseuse = masseuseRepo.findById(request.getMasseuseId()).orElse(null);
        if (masseuse == null) {
            return "Maserka ne obstaja.";
        }

        if (!reservation.getCustomer().getId().equals(request.getCustomerId())) {
            return "Nimate pravice do urejanja te rezervacije.";
        }

        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        LocalDateTime startDateTime = request.getStartTime();
        LocalDateTime endDateTime = request.getEndTime();

        if (startDateTime.toLocalTime().isBefore(openingTime) || endDateTime.toLocalTime().isAfter(closingTime)) {
            return "Izbran termin je izven delovnega časa!";
        }

        // posodobimo podatke
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setCustomer(customer);
        reservation.setMassage(massage);
        reservation.setMasseuse(masseuse);

        repo.save(reservation);

        return "Rezervacija uspešno posodobljena.";
    }


    public String deleteReservation(DeleteReservationRequest request) {
        Reservation reservation = repo.findById(request.getReservationId()).orElse(null);
        if (reservation == null) {
            return "Rezervacija ne obstaja.";
        }

        if (!reservation.getCustomer().getId().equals(request.getCustomerId())) {
            return "Nimate pravice do urejanja te rezervacije.";
        }

        repo.delete(reservation);
        return "Rezervacija uspešno izbrisana.";
    }


    /* 

    // general

    // pretvorba rezervacije z vsemi podatki v rezervacijo z delno prekritimi podatki
    public ReservationDTO transform(Reservation r){
        ReservationDTO reservationLight = new ReservationDTO(r.getId(), r.getStartTime(), r.getEndTime(), r.getMasseuse());
        return reservationLight; 
    }

    // metoda, ki najde vse obstojece rezervacije in jih vrze v ReservationDTO -> in potem vrne seznam transformiranih rezervacij

    public List<ReservationDTO> getAllLightReservations() {
        List<Reservation> existingReservations = repo.findAll();
        List<ReservationDTO> transformedReservations = new ArrayList<>();

        for (Reservation r : existingReservations) {
            ReservationDTO dto = transform(r);
            transformedReservations.add(dto);
        }

        return transformedReservations;
    }

    
    // metoda, ki vrne vse termine (proste in zasedene) */




    // receptionist 

    public List<Reservation> getFullReservation(){
        return repo.findAll();
    }

    // poizvedba terminov glede na maserko

    public List<Reservation> getReservationsByMasseuse(Long masseuseId) {
        return repo.findByMasseuseId(masseuseId);
    }

    // najdi rezervacijo po id-ju
    public ReservationDTO getReservation(Long id) {
        Reservation reservation = repo.findById(id).orElse(null);
        if (reservation == null) {
            return null;
        }

        ReservationDTO r = new ReservationDTO();
        r.setId(reservation.getId());
        r.setStartTime(reservation.getStartTime());
        r.setEndTime(reservation.getEndTime());
        r.setCustomerId(reservation.getCustomer().getId());
        r.setMassageId(reservation.getMassage().getId());
        r.setMasseuseId(reservation.getMasseuse().getId());

        return r;
    }


    // dodajanje rezervacij (samo receptor)

    public String addReservation(NewReservationRequest request) {
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        LocalDateTime startDateTime = request.getStartTime();
        LocalDateTime endDateTime = request.getEndTime();

        if (startDateTime.toLocalTime().isBefore(openingTime) || endDateTime.toLocalTime().isAfter(closingTime)) {
            return "Izbran termin je izven delovnega časa!";
        }

        Massage massage = massageRepo.findById(request.getMassageId()).orElseThrow(() -> new RuntimeException("Masaža ne obstaja."));
        Masseuse masseuse = masseuseRepo.findById(request.getMasseuseId()).orElseThrow(() -> new RuntimeException("Maserka ne obstaja."));

        // ustvarimo stranko
        Customer customer = new Customer();
        customer.setName(request.getCustomerName());
        customer.setSurname(request.getCustomerSurname());
        customer.setEmail(request.getCustomerEmail());
        customer.setContact(request.getCustomerPhone());

        customerRepo.save(customer);

        // preverimo če ima maserka že zaseden termin
        List<Reservation> existingReservations = repo.findByMasseuseId(masseuse.getId());
        for (Reservation r : existingReservations) {
            boolean overlap = r.getStartTime().isBefore(endDateTime) &&
                            startDateTime.isBefore(r.getEndTime());
            if (overlap) {
                return "Maserka za ta termin ni na voljo.";
            }
        }

        // ustvarimo inn shranimo novo rezervacijo
        Reservation reservation = new Reservation(startDateTime, endDateTime, customer, massage, masseuse);
        repo.save(reservation);

        return "Rezervacija uspešna!";
    }




    // spreminjanje rezervacije 

    public String editReservation(ReservationDTO dto) {
        Reservation existing = repo.findById(dto.getId()).orElse(null);
        if (existing == null) {
            return "Rezervacija ne obstaja.";
        }

        Customer customer = customerRepo.findById(dto.getCustomerId()).orElse(null);
        if (customer == null) {
            return "Stranka ne obstaja.";
        }

        Massage massage = massageRepo.findById(dto.getMassageId()).orElse(null);
        if (massage == null) {
            return "Masaža ne obstaja.";
        }

        Masseuse masseuse = masseuseRepo.findById(dto.getMasseuseId()).orElse(null);
        if (masseuse == null) {
            return "Maserka ne obstaja.";
        }

        boolean timeChanged =
                !existing.getStartTime().equals(dto.getStartTime()) ||
                !existing.getEndTime().equals(dto.getEndTime()) ||
                !existing.getMasseuse().getId().equals(dto.getMasseuseId());

        if (timeChanged) {
            List<Reservation> existingReservations = repo.findByMasseuseId(dto.getMasseuseId());
            for (Reservation r : existingReservations) {
                if (r.getId().equals(existing.getId())) continue; // ignoriramo trenutno rezervacijo

                boolean overlap = r.getStartTime().isBefore(dto.getEndTime()) && dto.getStartTime().isBefore(r.getEndTime());

                if (overlap) {
                    return "Maserka za ta termin ni na voljo.";
                }
            }
        }
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        LocalDateTime startDateTime = dto.getStartTime();
        LocalDateTime endDateTime = dto.getEndTime();

        if (startDateTime.toLocalTime().isBefore(openingTime) || endDateTime.toLocalTime().isAfter(closingTime)) {
            return "Izbran termin je izven delovnega časa!";
        }

        // posodobimo rezervacijo
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setCustomer(customer);
        existing.setMassage(massage);
        existing.setMasseuse(masseuse);

        repo.save(existing);
        return "Rezervacija uspešno urejena.";
    }


    // izbris rezervacije

    public String deleteReservation(Long id){
        repo.deleteById(id);
        return "Rezervacija je bila uspešno izbrisana";
    }


}
