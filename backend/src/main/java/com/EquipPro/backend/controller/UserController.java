package com.EquipPro.backend.controller;

import com.EquipPro.backend.exception.UserNotFoundException;
import com.EquipPro.backend.model.User;
import com.EquipPro.backend.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImp userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByEmail(@PathVariable Long userId){
        try {
            User user = userService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}
