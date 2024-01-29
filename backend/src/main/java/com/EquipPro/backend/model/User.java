package com.EquipPro.backend.model;

import com.EquipPro.backend.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EquipmentRequest> requestedEquipment = new ArrayList<>();

    @OneToMany(mappedBy = "currentUser" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Equipment> ownedEquipments = new ArrayList<>();

    @ManyToMany(mappedBy = "technicians")
    private List<Equipment> fixedEquipments = new ArrayList<>();

    @OneToMany(mappedBy = "currentTechnician", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
