package com.EquipPro.backend.service;

import com.EquipPro.backend.model.Equipment;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getAllEquipments();
    List<Equipment> getOwnedEquipments(Long userId);
    List<Equipment> getFixedEquipments(Long technicianId);
    Equipment createEquipment(String equipmentName, String serialEquipmentCode);
    List<Equipment> getFixingEquipment(Long technicianId);
    List<Equipment> getAvailableEquipment();
    Equipment getEquipment(Long equipmentId);
    void deleteEquipment(Long equipmentId);
    void assignEquipmentToUser(Long equipmentId , Long userId);
    void assignEquipmentToTechnician(Long equipmentId , Long technicianId);
    void removeEquipmentFromUser(Long equipmentId, Long userId);
    void removeEquipmentFromTechnician(Long equipmentId, Long technicianId);
    Equipment equipmentIssueRequest(Long equipmentId);
    List<Equipment> getCrushedEquipments();
}
