package com.ust.ticket_service.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "tickets")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private long ticketId;

    @Column(name = "ticket_price")
    private long ticketPrice;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "eventTicket_id")
    private long eventTicketId;
}
