package fis.prspr.MassageBooking.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receptionist {

    @Id @GeneratedValue
    private Long id; 

    @Column(unique = true)
    private String username; 
    
    private String password; 

    public Receptionist(String username, String password){
        this.username = username; 
        this.password = password; 
    }


}
