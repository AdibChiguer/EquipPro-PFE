package com.EquipPro.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String role;

    // for the user and admin to see all the requests
    @OneToMany(mappedBy = "worker")
    private List<EquipmentRequest> requestedEquipment = new ArrayList<>();


    // for the user to get all the equipment that his own
    @OneToMany(mappedBy = "currentUser")
    private List<Equipment> ownedEquipments = new ArrayList<>();

    // for the technician to see all the fixed equipment
    @ManyToMany(mappedBy = "technicians")
    private List<Equipment> fixedEquipments = new ArrayList<>();

    @OneToMany(mappedBy = "currentTechnician")
    private List<Equipment> equipmentInWork = new ArrayList<>();

    public void assignEquipmentToUser(Equipment equipment){
        this.ownedEquipments.add(equipment);
        equipment.setCurrentUser(this);
        equipment.setDateOfOwnership(LocalDate.now());
        equipment.setOwnershipStatus(true);
        equipment.setOwnedBy(this);
        equipment.setCurrentTechnician(null);
    }

    public void assignEquipmentToTechnician(Equipment equipment){
        equipment.setCurrentTechnician(this);
        equipment.setTechnicians(this);
    }

    public void removeEquipmentFromUser(Equipment equipment){
        if (ownedEquipments.contains(equipment)){
            ownedEquipments.remove(equipment);
            equipment.setCurrentUser(null);
            equipment.setDateOfOwnership(null);
            equipment.setOwnershipStatus(false);
        }
    }

    public void removeEquipmentFromTechnician(Equipment equipment){
        if(equipmentInWork.contains(equipment)){
            fixedEquipments.add(equipment);
            equipmentInWork.remove(equipment);
            equipment.setCurrentTechnician(null);
        }
    }

}
