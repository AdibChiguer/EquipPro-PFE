package com.EquipPro.backend.service;

import com.EquipPro.backend.exception.EquimpmentAlreadyExistsException;
import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.UserAlreadyExistsException;
import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.repository.EquipmentRepository;
import com.EquipPro.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentServiceImp implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }
    @Override
    public List<Equipment> getOwnedEquipments(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getOwnedEquipments).orElse(null);
    }
    @Override
    public List<Equipment> getFixedEquipments(Long technicianId) {
        return userRepository.getFixedEquipments(technicianId);
    }
    @Override
    public Equipment createEquipment(String equipmentName, String serialEquipmentCode) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName(equipmentName);
        equipment.setSerialEquipmentCode(serialEquipmentCode);
        return equipmentRepository.save(equipment);
    }
    @Override
    public List<Equipment> getFixingEquipment(Long technicianId) {
        return userRepository.getFixingEquipments(technicianId);
    }
    @Override
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findEquipmentByOwnershipStatus(false);
    }
    @Override
    public Equipment getEquipment(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException("equipment not found!"));
    }
    @Override
    public void deleteEquipment(Long equipmentId) {
        Equipment equipment = getEquipment(equipmentId);
        if(equipment != null){
            equipmentRepository.deleteById(equipmentId);
        }
    }
    @Override
    public void assignEquipmentToUser(Long equipmentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (user.isPresent() && user.get().getOwnedEquipments().contains(equipment.get())){
            throw new EquimpmentAlreadyExistsException(equipment.get().getEquipmentName()+ " is already assigned to the user: "+ user.get().getFullName());
        }
        if (equipment.isPresent() && user.isPresent()){
            user.get().assignEquipmentToUser(equipment.get());
            userRepository.save(user.get());
        }
    }
    @Override
    public void assignEquipmentToTechnician(Long equipmentId, Long technicianId) {
        Optional<User> technician = userRepository.findById(technicianId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (technician.isPresent() && technician.get().getEquipmentInWork().contains(equipment.get())){
            throw new EquimpmentAlreadyExistsException(equipment.get().getEquipmentName()+ " is already assigned to the technician: "+ technician.get().getFullName());
        }
        if (equipment.isPresent() && technician.isPresent()){
            technician.get().assignEquipmentToTechnician(equipment.get());
            userRepository.save(technician.get());
        }
    }
    @Override
    public void removeEquipmentFromUser(Long equipmentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (user.isPresent() && equipment.isPresent()){
            user.get().removeEquipmentFromUser(equipment.get());
        }
    }
    @Override
    public void removeEquipmentFromTechnician(Long equipmentId, Long technicianId) {
        Optional<User> technician = userRepository.findById(technicianId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (technician.isPresent() && equipment.isPresent()){
            technician.get().removeEquipmentFromTechnician(equipment.get());
        }
    }
}
