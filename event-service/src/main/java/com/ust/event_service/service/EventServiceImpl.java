package com.ust.event_service.service;

import com.ust.event_service.dao.EventRepository;
import com.ust.event_service.dao.entity.EventEntity;
import com.ust.event_service.model.EventPojo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    EventRepository eventRepo;

    public EventServiceImpl(EventRepository eventRepo) { this.eventRepo = eventRepo; }

    @Override
    public List<EventPojo> getAllEvents() {
        List<EventEntity> allEventEntity = eventRepo.findAll();
        List<EventPojo> allEventPojo = new ArrayList<>();
        allEventEntity.stream().forEach((eachEventEntity) -> {
            EventPojo eventPojo = new EventPojo();
            BeanUtils.copyProperties(eachEventEntity, eventPojo);
            allEventPojo.add(eventPojo);
        });
        return allEventPojo;
    }

    @Override
    public EventPojo getAEvent(long eventId) {
        Optional<EventEntity> fetchedEventEntity = eventRepo.findById(eventId);
        EventPojo eventPojo = null;
        if (fetchedEventEntity.isPresent()) {
            eventPojo = new EventPojo();
            BeanUtils.copyProperties(fetchedEventEntity.get(), eventPojo);
        }
        return eventPojo;
    }

    @Override
    public EventPojo addEvent(EventPojo newEvent) {
        EventEntity eventEntity = new EventEntity();
        BeanUtils.copyProperties(newEvent, eventEntity);
        eventRepo.saveAndFlush(eventEntity);
        return newEvent;
    }

    @Override
    public EventPojo updateEvent(EventPojo updatedEvent) {
        EventEntity eventEntity = new EventEntity();
        BeanUtils.copyProperties(updatedEvent, eventEntity);
        eventRepo.saveAndFlush(eventEntity);
        return updatedEvent;
    }

    @Override
    public void deleteEvent(long eventId) {
        eventRepo.deleteById(eventId);
    }
}
