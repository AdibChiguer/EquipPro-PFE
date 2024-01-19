package com.EquipPro.backend.service;

import com.EquipPro.backend.model.EquipmentRequest;
import org.aspectj.apache.bcel.classfile.LineNumber;

import java.util.List;

public interface EquipmentRequestService {
    List<EquipmentRequest> getAllEquipmentRequest();
    EquipmentRequest getEquipmentRequest(Long equipmentRequestId);
    void createEquipmentRequest(Long userId, Long equipmentId);
    void deleteEquipmentRequest(Long equipmentRequestId);
}
