package com.ust.event_service.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @Column(name = "event id")
    private long event_id;

    @Column(name = "event_name")
    private String eventName;
}
