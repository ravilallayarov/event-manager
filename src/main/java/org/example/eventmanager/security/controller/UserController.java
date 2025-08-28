package org.example.eventmanager.security.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventmanager.security.dto.*;
import org.example.eventmanager.security.mapper.UserMapper;
import org.example.eventmanager.security.model.User;
import org.example.eventmanager.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User savedUser = userService.registerUser(registerRequest);
        UserDto userDto = userMapper.toDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authUser(@Valid @RequestBody AuthRequest authRequest) {
        String jwt = userService.authUser(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
