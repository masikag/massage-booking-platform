package fis.prspr.MassageBooking.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmReservationRequest {
    private String email;
    private String password;
    private Long massageId;
    private Long masseuseId;
    private LocalDate date;
    private LocalTime time;
}
