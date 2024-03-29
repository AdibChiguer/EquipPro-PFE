package com.EquipPro.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
public class Equipment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "equipment_name")
    private String equipmentName;
    @Column(name = "serial_equipment_code")
    private String serialEquipmentCode;
    @Column(name = "ownership_status")
    private boolean ownershipStatus;
    @Column(name = "issue_status")
    private boolean issueStatus;
    @Column(name = "date_of_ownership")
    private LocalDate dateOfOwnership;
    @Column(name = "crash_count")
    private int crashCount;

    @ManyToOne(cascade = CascadeType.ALL)
    private User currentUser;

    @ManyToMany
    @JoinTable(
            name = "equipment_previous_owners",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> ownedBy = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "equipment_technicians",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "technician_id")
    )
    private List<User> technicians = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_technician_id")
    private User currentTechnician;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket tickets;

    public void setOwnedBy(User user) {
        this.ownedBy.add(user);
    }
    public void setTechnicians(User technician) {
        this.technicians.add(technician);
    }
    public Equipment() {
        this.crashCount = 0;
    }
}
