package ru.adel.apigateway.core.service.authentication.db.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email " + email));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found with id " + id));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
