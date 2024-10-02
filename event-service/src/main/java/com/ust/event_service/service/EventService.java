package com.ust.event_service.service;

import com.ust.event_service.model.EventPojo;

import java.util.List;

public interface EventService {
    List<EventPojo> getAllEvents();
    EventPojo getAEvent(long eventId);
    EventPojo addEvent(EventPojo newEvent);
    EventPojo updateEvent(EventPojo updatedEvent);
    void deleteEvent(long eventId);
}
