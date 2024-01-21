package com.EquipPro.backend.service;

import com.EquipPro.backend.model.Ticket;
import org.springframework.beans.propertyeditors.LocaleEditor;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface TicketService {
    List<Ticket> getAlltickets();
    Ticket getTicket(Long ticketId);
    Ticket createTicket(LocalDate takingTime, Long equipmentId, Long ownerId, Long technicianId);
    void deleteTicket(Long ticketId);
}
