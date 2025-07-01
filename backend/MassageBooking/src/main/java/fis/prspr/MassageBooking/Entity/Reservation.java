package fis.prspr.MassageBooking.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id @GeneratedValue
    private Long id; 

    private LocalDateTime startTime; 
    private LocalDateTime endTime; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("reservations")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("reservations")
    @JoinColumn(name = "massage_id")
    private Massage massage;

    @ManyToOne
    @JsonIgnoreProperties("reservations") 
    private Masseuse masseuse;


    public Reservation(LocalDateTime startTime, LocalDateTime endTime, Customer customer, Massage massage, Masseuse masseuse){
        this.startTime = startTime; 
        this.endTime = endTime;
        this.customer = customer; 
        this.massage = massage; 
        this.masseuse = masseuse; 
    
    }
}
