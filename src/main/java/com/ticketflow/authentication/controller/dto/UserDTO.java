package com.ticketflow.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private boolean enabled;

    private Map<String, Object> attributes;

    private Set<String> groups;

    private String email;

    private boolean emailVerified;

    private String firstName;

    private String lastName;

    private String username;

    private Set<UserCredentialDTO> credentials;

}
