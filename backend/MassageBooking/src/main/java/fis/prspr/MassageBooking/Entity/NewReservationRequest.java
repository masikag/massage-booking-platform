package fis.prspr.MassageBooking.Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReservationRequest {
    public String customerName;
    public String customerSurname;
    public String customerEmail;
    public String customerPhone;
    
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Long massageId;
    public Long masseuseId;
}

