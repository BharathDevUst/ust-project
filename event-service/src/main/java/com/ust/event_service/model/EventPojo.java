package com.ust.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventPojo {
    private long eventId;
    private String eventName;
    private List<EventPojo> allEvents;
}
