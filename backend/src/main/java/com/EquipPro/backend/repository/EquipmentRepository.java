package com.EquipPro.backend.repository;

import com.EquipPro.backend.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findEquipmentByOwnershipStatus(Boolean status);
}
