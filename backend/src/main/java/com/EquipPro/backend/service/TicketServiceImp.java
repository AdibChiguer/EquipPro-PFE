package com.EquipPro.backend.service;

import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.TicketNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.Ticket;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.repository.EquipmentRepository;
import com.EquipPro.backend.repository.TicketRepository;
import com.EquipPro.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImp implements TicketService{
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public List<Ticket> getAlltickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("The ticket not found"));
    }

    @Override
    public Ticket createTicket(LocalDate takingTime, Long equipmentId, Long ownerId, Long technicianId) {
        Ticket ticket = new Ticket();
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        Optional<User> owner = userRepository.findById(ownerId);
        Optional<User> technician = userRepository.findById(technicianId);

        if (equipment.isEmpty()){
            throw new EquipmentNotFoundException("Equipment not found");
        } else if (owner.isEmpty()) {
            throw new UserNotFoundException("Owner not found");
        } else if (technician.isEmpty()) {
            throw new UserNotFoundException("Technician not found");
        } else {
            ticket.setTakingTime(takingTime);
            ticket.setEquipment(equipment.get());
            ticket.setEquipmentOwner(owner.get());
            ticket.setTechnician(technician.get());
            return ticketRepository.save(ticket);
        }
    }

    @Override
    public void deleteTicket(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()){
            throw new TicketNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(ticketId);
    }
}
