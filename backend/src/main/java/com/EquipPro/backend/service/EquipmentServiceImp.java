package com.EquipPro.backend.service;

import com.EquipPro.backend.exception.EquimpmentAlreadyExistsException;
import com.EquipPro.backend.exception.EquipmentNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
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
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getOwnedEquipments();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
    @Override
    public List<Equipment> getFixedEquipments(Long technicianId) {
        Optional<User> technician = userRepository.findById(technicianId);
        if (technician.isPresent()){
            return userRepository.getFixedEquipments(technicianId);
        } else {
            throw new UserNotFoundException("User not found");
        }
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
        Optional<User> technician = userRepository.findById(technicianId);
        if (technician.isPresent()){
            return userRepository.getFixingEquipments(technicianId);
        } else {
            throw new UserNotFoundException("User not found");
        }
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
        } else {
            throw new EquipmentNotFoundException("equipment not found with the id : "+ equipmentId);
        }
    }
    @Override
    public void assignEquipmentToUser(Long equipmentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not found");
        } else if (equipment.isEmpty()){
            throw new EquipmentNotFoundException("Equipment not found");
        } else if (user.get().getOwnedEquipments().contains(equipment.get())){
            throw new EquimpmentAlreadyExistsException(equipment.get().getEquipmentName()+ " is already assigned to the user: "+ user.get().getFullName());
        } else {
            user.get().assignEquipmentToUser(equipment.get());
            userRepository.save(user.get());
        }
    }
    @Override
    public void assignEquipmentToTechnician(Long equipmentId, Long technicianId) {
        Optional<User> technician = userRepository.findById(technicianId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (technician.isEmpty()){
            throw new UserNotFoundException("technician not found");
        } else if (equipment.isEmpty()){
            throw new EquipmentNotFoundException("equipment not found");
        } else if (technician.get().getEquipmentInWork().contains(equipment.get())){
            throw new EquimpmentAlreadyExistsException(equipment.get().getEquipmentName()+ " is already assigned to the technician: "+ technician.get().getFullName());
        }
        technician.get().assignEquipmentToTechnician(equipment.get());
        userRepository.save(technician.get());
    }

    @Override
    public Equipment equipmentIssueRequest(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if(equipment.isEmpty()){
            throw new EquipmentNotFoundException("equipment not found");
        }
        equipment.get().setIssueStatus(true);
        return equipment.get();
    }

    @Override
    public void removeEquipmentFromUser(Long equipmentId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not found");
        } else if (equipment.isEmpty()){
            throw new EquipmentNotFoundException("Equipment not found");
        } else {
            user.get().removeEquipmentFromUser(equipment.get());
        }
    }

    @Override
    public void removeEquipmentFromTechnician(Long equipmentId, Long technicianId) {
        Optional<User> technician = userRepository.findById(technicianId);
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (technician.isEmpty()){
            throw new UserNotFoundException("Technician not found");
        } else if (equipment.isEmpty()){
            throw new EquipmentNotFoundException("Equipment not found");
        } else {
            technician.get().removeEquipmentFromTechnician(equipment.get());
        }
    }
}
