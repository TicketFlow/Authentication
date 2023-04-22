package com.ticketflow.authentication.controller;

import com.ticketflow.authentication.controller.dto.UserCreationDTO;
import com.ticketflow.authentication.controller.dto.UserLoginDTO;
import com.ticketflow.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    @GetMapping
    public String getToken(@RequestBody UserLoginDTO userDTO) {
        return userService.getToken(userDTO);
    }

    @PostMapping
    public UserCreationDTO createUser(@RequestBody UserCreationDTO userCreationDTO) {
        return userService.buildAndCreateUser(userCreationDTO);
    }

}
