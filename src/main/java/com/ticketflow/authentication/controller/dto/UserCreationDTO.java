package com.ticketflow.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private LocalDate birthDate;
}
