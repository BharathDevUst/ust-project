package com.ust.ticket_service.dao;

import com.ust.ticket_service.dao.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByEventTicketId(long eventId);
    List<TicketEntity> findByUserId(long userId);
}
