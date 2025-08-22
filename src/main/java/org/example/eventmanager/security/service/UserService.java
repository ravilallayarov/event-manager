package org.example.eventmanager.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.exception.AlreadyExistsException;
import org.example.eventmanager.exception.NotFoundException;
import org.example.eventmanager.security.dto.AuthRequest;
import org.example.eventmanager.security.dto.RegisterRequest;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.mapper.UserMapper;
import org.example.eventmanager.security.model.User;
import org.example.eventmanager.security.repository.UserRepository;
import org.example.eventmanager.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public User registerUser(RegisterRequest registerRequest) {
        log.info("trying to register a new user with login: {}", registerRequest.getLogin());
        if(userRepository.existsByLogin(registerRequest.getLogin())) {
            throw new AlreadyExistsException("This login is already taken");
        }
        UserEntity userToSave = userMapper.toEntity(registerRequest, passwordEncoder);
        UserEntity savedUser = userRepository.save(userToSave);

        log.info("new user successfully created with login: {}", registerRequest.getLogin());
        return userMapper.toUser(savedUser);
    }

    public String authUser(AuthRequest authRequest) {
        log.info("trying to auth user with login: {}", authRequest.getLogin());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(), authRequest.getPassword()
        ));
        return jwtUtil.generateJwt(authRequest.getLogin());
    }

    public User findByLogin(String login) {
        log.info("trying to find a user with login: {}", login);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException("User with login: " + login + " not found"));
        return userMapper.toUser(userEntity);
    }

    public User findById(Long id) {
        log.info("trying to find a user by id: {}", id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id: " + id + " not found"));
        return userMapper.toUser(userEntity);
    }
}
