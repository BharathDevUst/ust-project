package com.ust.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketPojo {
    private long ticketId;
    private long ticketPrice;
    private long eventId;
}
