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
public class Customer {

    @Id @GeneratedValue
    private Long id;
    
    private String name;
    private String surname; 
    private String contact;
    
    @Column(unique = true)
    private String email;
    private String password; 

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Reservation> reservations;


    public Customer (String name, String surname, String contact, String email, String password){
        this.name = name; 
        this.surname = surname; 
        this.contact = contact; 
        this.email = email;
        this.password = password;
    }

}
