package com.EquipPro.backend.service;

import com.EquipPro.backend.model.Equipment;
import com.EquipPro.backend.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(String email);
    void deleteUser(String email);
    User registerUser(User user);
}
