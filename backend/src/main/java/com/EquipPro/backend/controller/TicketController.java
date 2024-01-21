package com.EquipPro.backend.controller;

import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.TicketNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.Ticket;
import com.EquipPro.backend.service.TicketServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketServiceImp ticketServiceImp;

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return ResponseEntity.ok(ticketServiceImp.getAlltickets());
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<?> getTicket(@PathVariable("ticketId") Long ticketId){
        try {
            return ResponseEntity.ok(ticketServiceImp.getTicket(ticketId));
        } catch (TicketNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error getting the ticket");
        }
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket){

        try {
            return ResponseEntity.ok(ticketServiceImp.createTicket(ticket.getTakingTime(),
                    ticket.getEquipment().getId(),
                    ticket.getEquipmentOwner().getId(),
                    ticket.getTechnician().getId()));
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the ticket");
        }
    }

    @DeleteMapping("/delete/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId){
        try {
            ticketServiceImp.deleteTicket(ticketId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Ticket deleted successfully");
        } catch (TicketNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the ticket");
        }
    }
}
