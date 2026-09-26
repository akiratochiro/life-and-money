package com.akiratochiro.life_and_money_api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String name, String email, String rawPassword){
        String normalizedEmail = User.normalizeEmail(email);

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new EmailAlreadyInUseException(normalizedEmail);
        }

        String passwordHash = passwordEncoder.encode(rawPassword);
        User user = new User(name, normalizedEmail, passwordHash);
        return userRepository.save(user);
    }

}
