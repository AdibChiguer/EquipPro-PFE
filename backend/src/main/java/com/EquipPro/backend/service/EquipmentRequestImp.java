package com.EquipPro.backend.service;

import com.EquipPro.backend.exception.EquipmentRequestNotFoundException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.EquipmentRequest;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.repository.EquipmentRequestRepository;
import com.EquipPro.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentRequestImp implements EquipmentRequestService{
    private final EquipmentRequestRepository equipmentRequestRepository;
    private final EquipmentServiceImp equipmentServiceImp;
    private final UserRepository userRepository;

    @Override
    public List<EquipmentRequest> getAllEquipmentRequest() {
        return equipmentRequestRepository.findAll();
    }

    @Override
    public EquipmentRequest getEquipmentRequest(Long equipmentRequestId) {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(equipmentRequestId)
                .orElseThrow(()-> new EquipmentRequestNotFoundException("The equipment request not found!"));
        equipmentRequest.setRequestStatus(true);
        return equipmentRequest;
    }

    @Override
    public EquipmentRequest createEquipmentRequest(Long userId, Long equipmentId) {
        EquipmentRequest equipmentRequest = new EquipmentRequest();
        Equipment equipment = equipmentServiceImp.getEquipment(equipmentId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        equipmentRequest.setEquipment(equipment);
        equipmentRequest.setWorker(user.get());
        equipmentRequest.setRequestStatus(false);
        return equipmentRequest;
    }

    @Override
    public void deleteEquipmentRequest(Long equipmentRequestId) {
        Optional<EquipmentRequest> equipmentRequest = equipmentRequestRepository.findById(equipmentRequestId);
        if (equipmentRequest.isEmpty()){
            throw new EquipmentRequestNotFoundException("Equipment Request not found");
        }
        equipmentRequestRepository.deleteById(equipmentRequestId);
    }
}
