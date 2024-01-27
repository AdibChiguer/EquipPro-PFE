package com.EquipPro.backend.controller;

import com.EquipPro.backend.exception.UserAlreadyExistsException;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImp userServiceImp;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user){
        try{
            userServiceImp.registerUser(user);
            return ResponseEntity.ok("Registration successful!");
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
