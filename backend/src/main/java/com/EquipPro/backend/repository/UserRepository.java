package com.EquipPro.backend.repository;

import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT e FROM Equipment e JOIN e.technicians t WHERE t.id = :technicianId")
    List<Equipment> getFixedEquipments(@Param("technicianId") Long technicianId);
    @Query("SELECT e FROM Equipment e WHERE e.currentTechnician.id = :technicianId")
    List<Equipment> getFixingEquipments(@Param("technicianId") Long technicianId);
    void deleteByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
