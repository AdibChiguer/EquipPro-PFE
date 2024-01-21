package com.EquipPro.backend.controller;

import com.EquipPro.backend.model.Ticket;
import com.EquipPro.backend.service.TicketServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketServiceImp ticketServiceImp;

    public ResponseEntity<List<Ticket>> getAllTickets(){
        return ResponseEntity.ok(ticketServiceImp.getAlltickets());
    }

    public ResponseEntity<Ticket> getTicket(@PathVariable("ticketId") Long ticketId){
        return ResponseEntity.ok(ticketServiceImp.getTicket(ticketId));
    }

    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket){
        return ResponseEntity.ok(ticketServiceImp.createTicket(ticket.getTakingTime(),
                ticket.getEquipment().getId(),
                ticket.getEquipmentOwner().getId(),
                ticket.getTechnician().getId()));
    }

//    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId){
//
//    }


}
