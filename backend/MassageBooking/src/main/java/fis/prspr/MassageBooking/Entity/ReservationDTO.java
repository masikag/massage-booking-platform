package fis.prspr.MassageBooking.Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Long customerId;
    public Long massageId;
    public Long masseuseId;
}

