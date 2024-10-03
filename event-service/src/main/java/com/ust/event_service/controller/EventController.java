package com.ust.event_service.controller;

import com.ust.event_service.model.EventPojo;
import com.ust.event_service.model.TicketPojo;
import com.ust.event_service.service.EventService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {
    EventService eventService;
    public static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public List<EventPojo> getAllEvents() {
        LOG.info("in getAllEvents");
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{eventId}")
    @CircuitBreaker(name = "eventTkt", fallbackMethod = "eventFallback")
    public EventPojo getAEvent(@PathVariable("eventId") long eventId) {
        LOG.info("in getAEvent");
        EventPojo eventPojo = eventService.getAEvent(eventId);
        WebClient.Builder webclient = WebClient.builder();
        List<TicketPojo> allTickets = webclient.build()
                .get()
                .uri("http://localhost:8082/api/tickets/" + eventId)
                .retrieve()
                .bodyToFlux(TicketPojo.class)
                .collectList()
                .block();
        eventPojo.setAllTickets(allTickets);
        return eventPojo;
    }

    public EventPojo eventFallback() {
        return new EventPojo(0,"fallback", null);
    }

    @PostMapping("/events")
    @CircuitBreaker(name = "eventTkt", fallbackMethod = "eventFallback")
    public EventPojo addEvent(@RequestBody EventPojo newEvent) {
        LOG.info("in addEvent");
        return eventService.addEvent(newEvent);
    }

    @PutMapping("/events/{eventId}")
    @CircuitBreaker(name = "eventTkt", fallbackMethod = "eventFallback")
    public EventPojo updateEvent(@RequestBody EventPojo updatedEvent) {
        LOG.info("in update Event");
        return eventService.updateEvent(updatedEvent);
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable("eventId") long eventId) {
        LOG.info("in delete Event");
        eventService.deleteEvent(eventId);
    }

    @PostMapping("/events/{id}/register")
    @CircuitBreaker(name = "eventTkt", fallbackMethod = "eventFallback")
    public long registerUser(@PathVariable("eventId") long eventId, @RequestBody TicketPojo newTicket) {
        LOG.info("Registering the user for event");
        WebClient.Builder webClient = WebClient.builder();
        TicketPojo ticket = webClient.build().post().uri("http://localhost:8082/api/tickets/generate").bodyValue(newTicket).retrieve().bodyToMono(TicketPojo.class).block();
        if (ticket != null)
            return ticket.getTicketId();
        else
            return -1;
    }
}
