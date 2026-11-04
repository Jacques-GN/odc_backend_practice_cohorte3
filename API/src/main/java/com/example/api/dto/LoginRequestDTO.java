package com.example.api.dto;

import jakarta.*;
import lombok.*;

@Data
@NoArgsContructor
@AllArgsContructor
public class LoginRequestDTO{

    @NotBlank(message = "l'emeil est obligatoir")
    private String email;
    
    @NotBlank(message = "le mode passe  est obligatoir")    
    @Size(min = 8, message("le mode passe doi contenier en moyenne 8 carractaire"))
    private String password;
}