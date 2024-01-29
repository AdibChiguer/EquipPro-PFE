package com.EquipPro.backend.controller;

import com.EquipPro.backend.exception.EquimpmentAlreadyExistsException;
import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Equipment>> getAllEquipment(){
        return new ResponseEntity<>(equipmentService.getAllEquipments() , HttpStatus.FOUND);
    }

    @GetMapping("/owned-equipment/{userId}")
    public ResponseEntity<?> getOwnedEquipments(@PathVariable Long userId){
        try {
            return new ResponseEntity<>(equipmentService.getOwnedEquipments(userId), HttpStatus.FOUND);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the equipments");
        }

    }

    @GetMapping("/fixed-equipment/{technicianId}")
    public ResponseEntity<?> getFixedEquipments(@PathVariable Long technicianId){
        try {
            return new ResponseEntity<>(equipmentService.getFixedEquipments(technicianId), HttpStatus.FOUND);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the equipments");
        }

    }

    @GetMapping("/fixing-equipment/{technicianId}")
    public ResponseEntity<?> getFixingEquipment(@PathVariable Long technicianId){
        try {
            return new ResponseEntity<>(equipmentService.getFixingEquipment(technicianId), HttpStatus.FOUND);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the equipments");
        }
    }

    @PostMapping("/create-equipment")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment reqEquipment){
        Equipment equipment = equipmentService.
                createEquipment(reqEquipment.getEquipmentName(), reqEquipment.getSerialEquipmentCode());
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/available-equipments")
    public ResponseEntity<List<Equipment>> getAvailableEquipments(){
        return ResponseEntity.ok(equipmentService.getAvailableEquipment());
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<?> getEquipment(@PathVariable Long equipmentId){
        try {
            return ResponseEntity.ok(equipmentService.getEquipment(equipmentId));
        } catch (EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the equipment");
        }
    }

    @DeleteMapping("/delete/equipment/{equipmentId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long equipmentId){
        try{
            equipmentService.deleteEquipment(equipmentId);
            return ResponseEntity.status(HttpStatus.OK).body("Equipment deleted successfully");
        } catch (EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the equipment");
        }
    }

    @PostMapping("/assign-equipment-to-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignEquipmentToUser(@RequestParam("userId") Long userId,
                                                        @RequestParam("equipmentId") Long equipmentId){
        try {
            equipmentService.assignEquipmentToUser(equipmentId , userId);
            return ResponseEntity.ok("The equipment assigned successfully");
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EquimpmentAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning the equipment to the user");
        }
    }
    @PostMapping("/assign-crashed-equipment-to-technician")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignCrashedEquipmentToTechnician(@RequestParam("technicianId") Long technicianId,
                                                              @RequestParam("equipmentId") Long equipmentId){
        try {
            equipmentService.assignEquipmentToTechnician(equipmentId, technicianId);
            return ResponseEntity.ok("The equipment assigned successfully");
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EquimpmentAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning the equipment to the user");
        }
    }

    @PostMapping("/remove-equipment-from-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeEquipmentFromUser(@RequestParam("userId") Long userId,
                                                          @RequestParam("equipmentId") Long equipmentId){
        try {
            equipmentService.removeEquipmentFromUser(equipmentId, userId);
            return ResponseEntity.ok("The equipment removed from the user successfully");
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing equipment from the user");
        }
    }

    @PostMapping("/remove-equipment-from-technician")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeEquipmentFromTechnician(@RequestParam("technicianId") Long technicianId,
                                                                @RequestParam("equipmentId") Long equipmentId){
        try {
            equipmentService.removeEquipmentFromTechnician(equipmentId, technicianId);
            return ResponseEntity.ok("The fixed equipment removed from the technician successfully");
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing equipment from the user");
        }
    }
    @PostMapping("/equipment-issue-request/{equipmentId}")
    public ResponseEntity<?> equipmentIssueRequest(@PathVariable("equipmentId") Long equipmentId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(equipmentService.equipmentIssueRequest(equipmentId));
        } catch (EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error making the request issue");
        }
    }

    @GetMapping("/crushed-equipments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Equipment>> getCrushedEquipments(){
        return ResponseEntity.ok(equipmentService.getCrushedEquipments());
    }
}