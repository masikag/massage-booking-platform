package fis.prspr.MassageBooking.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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
public class Masseuse {

    @Id @GeneratedValue
    private Long id; 

    @Column(unique = true)
    private String masseuseName; 

    @OneToMany(mappedBy = "masseuse")
    @JsonIgnore
    private List<Reservation> reservations;


    public Masseuse(String masseuseName, List<Reservation> reservations){
        this.masseuseName = masseuseName; 
        this.reservations =reservations;
    }

}
