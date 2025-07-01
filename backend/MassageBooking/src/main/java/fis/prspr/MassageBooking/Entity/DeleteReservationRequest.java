package fis.prspr.MassageBooking.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReservationRequest {
    private Long reservationId;
    private Long customerId;
}
