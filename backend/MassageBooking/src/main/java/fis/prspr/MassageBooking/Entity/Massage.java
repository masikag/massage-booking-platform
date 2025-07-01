package fis.prspr.MassageBooking.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Massage {

    @Id @GeneratedValue
    private Long id; 
    private String massageName; 
    private int duration;
    private double price;

    @OneToMany(mappedBy = "massage")
    @JsonIgnore
    private List<Reservation> reservations;


    public Massage(String massageName, int duration, double price){
        this.massageName = massageName; 
        this.duration = duration; 
        this.price = price; 
    }
    

}
