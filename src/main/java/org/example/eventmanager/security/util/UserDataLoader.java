package org.example.eventmanager.security.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.eventmanager.security.entity.Role;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void dataLoader() {
        checkUser("user", "user", Role.USER);
        checkUser("admin", "admin", Role.ADMIN);
    }

    private void checkUser(String login, String password, Role role) {
        if(!(userRepository.existsByLogin(login))) {
            UserEntity user = new UserEntity(null, login,
                    passwordEncoder.encode(password), 18, role);
            userRepository.save(user);
        }
    }
}
