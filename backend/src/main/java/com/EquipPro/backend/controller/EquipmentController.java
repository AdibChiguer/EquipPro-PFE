package com.EquipPro.backend.controller;

import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @GetMapping("/all")
    public ResponseEntity<List<Equipment>> getAllEquipment(){
        return new ResponseEntity<>(equipmentService.getAllEquipments() , HttpStatus.FOUND);
    }

    @GetMapping("/owned-equipment/{userId}")
    public ResponseEntity<List<Equipment>> getOwnedEquipments(@PathVariable Long userId){
        return new ResponseEntity<>(equipmentService.getOwnedEquipments(userId), HttpStatus.FOUND);
    }

    @GetMapping("/fixed-equipment/{technicianId}")
    public ResponseEntity<List<Equipment>> getFixedEquipments(@PathVariable Long technicianId){
        return new ResponseEntity<>(equipmentService.getFixedEquipments(technicianId), HttpStatus.FOUND);
    }

    @GetMapping("/fixing-equipment/{technicianId}")
    public ResponseEntity<List<Equipment>> getFixingEquipment(@PathVariable Long technicianId){
        return new ResponseEntity<>(equipmentService.getFixingEquipment(technicianId), HttpStatus.FOUND);
    }

    @PostMapping("/create-equipment")
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
    public ResponseEntity<Equipment> getEquipment(@PathVariable Long equipmentId){
        return ResponseEntity.ok(equipmentService.getEquipment(equipmentId));
    }

    @DeleteMapping("/delete/equipment/{equipmentId}")
    public void deleteEquipment(@PathVariable Long equipmentId){
        equipmentService.deleteEquipment(equipmentId);
    }

    @PostMapping("/assign-equipment-to-user")
    public ResponseEntity<String> assignEquipmentToUser(@RequestParam("userId") Long userId,
                                                        @RequestParam("equipmentId") Long equipmentId){
        equipmentService.assignEquipmentToUser(equipmentId , userId);
        return ResponseEntity.ok("The equipment assigned successfully");
    }
    @PostMapping("/assign-crashed-equipment-to-technician")
    public ResponseEntity<String> assignCrashedEquipmentToTechnician(@RequestParam("technicianId") Long technicianId,
                                                              @RequestParam("equipmentId") Long equipmentId){
        equipmentService.assignEquipmentToTechnician(equipmentId, technicianId);
        return ResponseEntity.ok("The equipment assigned successfully");
    }

    @PostMapping("/remove-equipment-from-user")
    public ResponseEntity<String> removeEquipmentFromUser(@RequestParam("userId") Long userId,
                                                          @RequestParam("equipmentId") Long equipmentId){
        equipmentService.removeEquipmentFromUser(equipmentId, userId);
        return ResponseEntity.ok("The equipment removed from the user successfully");
    }

    @PostMapping("/remove-equipment-from-technician")
    public ResponseEntity<String> removeEquipmentFromTechnician(@RequestParam("technicianId") Long technicianId,
                                                                @RequestParam("equipmentId") Long equipmentId){
        equipmentService.removeEquipmentFromTechnician(equipmentId, technicianId);
        return ResponseEntity.ok("The fixed equipment removed from the technician successfully");
    }
}