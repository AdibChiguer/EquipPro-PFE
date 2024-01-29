package com.EquipPro.backend.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@NotBlank
public class JwtResponse {
    private Long id;
    private String email;
    private String token;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(Long id, String email, String token, List<String> roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
