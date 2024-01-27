package com.EquipPro.backend.service;

import com.EquipPro.backend.exception.UserAlreadyExistsException;
import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.repository.EquipmentRepository;
import com.EquipPro.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EquipmentServiceImp equipmentServiceImp;
    private final EquipmentRepository equipmentRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @Override
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Equipment> equipmentList = equipmentServiceImp.getAllEquipments();
            for (Equipment equipment : equipmentList) {
                equipment.getOwnedBy().remove(user.get());
                equipment.getTechnicians().remove(user.get());
                if(equipment.getCurrentUser().getId() == user.get().getId()){
                    equipment.setCurrentUser(null);
                } else if( equipment.getCurrentTechnician().getId() == user.get().getId()){
                    equipment.setCurrentTechnician(null);
                }
                equipmentRepository.save(equipment);
            }
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }
}
