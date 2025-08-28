package org.example.eventmanager.security.service;

import lombok.RequiredArgsConstructor;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("User with login: " + username + " not found"));

        return User.withUsername(username)
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();
    }
}
