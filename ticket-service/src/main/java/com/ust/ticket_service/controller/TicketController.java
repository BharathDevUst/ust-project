package com.ust.ticket_service.controller;

import com.ust.ticket_service.dao.TicketRepository;
import com.ust.ticket_service.dao.entity.TicketEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {
    public static final Logger LOG = LoggerFactory.getLogger(TicketController.class);
    TicketRepository ticketRepo;
    public TicketController(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @GetMapping("/tickets/{eventId}")
    public List<TicketEntity> getAllTicketsByEvent(@PathVariable("eventId") long eventId) {
        return ticketRepo.findByEventTicketId(eventId);
//        return null;
    }

    @GetMapping("/tickets/user/{userId}")
    public List<TicketEntity> getTicketsByUserId(@PathVariable("userId") long userId) {
        return ticketRepo.findByUserId(userId);
    }

    @PostMapping("/tickets/generate")
    public TicketEntity generateTicket(@RequestBody TicketEntity newTkt) {
        return ticketRepo.saveAndFlush(newTkt);
    }
}
