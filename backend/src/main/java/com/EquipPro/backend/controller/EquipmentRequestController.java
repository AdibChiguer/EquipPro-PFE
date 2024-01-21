package com.EquipPro.backend.controller;

import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.EquipmentRequestNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.EquipmentRequest;
import com.EquipPro.backend.service.EquipmentRequestImp;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipment-requests")
public class EquipmentRequestController {
    private final EquipmentRequestImp equipmentRequestImp;

    @GetMapping("/all")
    public ResponseEntity<List<EquipmentRequest>> getAllEquipmentRequests(){
        return ResponseEntity.ok(equipmentRequestImp.getAllEquipmentRequest());
    }

    @GetMapping("/equipment-request/{equipmentRequestId}")
    public ResponseEntity<?> getEquipmentRequest(
            @PathVariable("equipmentRequestId") Long equipmentRequestId){
        try {
            return ResponseEntity.ok(equipmentRequestImp.getEquipmentRequest(equipmentRequestId));
        } catch (EquipmentRequestNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the equipment request");
        }
    }

    @PostMapping("/create-equipment-request")
    public ResponseEntity<?> createEquipmentRequest(@RequestParam("userId") Long userId,
                                                    @RequestParam("equipmentId") Long equipmentId){
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(equipmentRequestImp.createEquipmentRequest(userId, equipmentId));
        } catch (UserNotFoundException | EquipmentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the equipment request");
        }
    }

    @DeleteMapping("/delete/{equipmentRequestId}")
    public ResponseEntity<String> deleteEquipmentRequest(
            @PathVariable("equipmentRequestId") Long equipmentRequestId){
        try {
            equipmentRequestImp.deleteEquipmentRequest(equipmentRequestId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("equipment request deleted successfully");
        } catch (EquipmentRequestNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("equipment request not found");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the equipment request");
        }
    }

}
