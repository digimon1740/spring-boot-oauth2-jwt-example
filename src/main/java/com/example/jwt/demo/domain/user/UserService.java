package com.example.jwt.demo.domain.user;

import com.example.jwt.demo.domain.message.MessageSourceService;
import com.example.jwt.demo.web.error.exception.ResourceExistsException;
import com.example.jwt.demo.web.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private MessageSourceService messageSourceService;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageSourceService messageSourceService) {
        this.userRepository = userRepository;
        this.messageSourceService = messageSourceService;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByPrincipal(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> findByName(p.getName()))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByNameOrElseThrow(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(messageSourceService.getMessage("user.not.exist")));
    }

    @Transactional
    public User create(User user) {
        Assert.notNull(user, messageSourceService.getMessage("user.name.invalid"));
        Assert.hasText(user.getName(), messageSourceService.getMessage("user.name.invalid"));
        Assert.hasText(user.getPassword(), messageSourceService.getMessage("user.password.invalid"));

        User exists = findByName(user.getName());
        if (exists != null) {
            throw new ResourceExistsException(messageSourceService.getMessage("user.exists"));
        }

        User newUser = User.builder()
                .name(user.getName())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .createdAt(LocalDateTime.now()).build();
        return userRepository.save(newUser);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void stampLastConnectedAt(String name) {
        Assert.hasText(name, messageSourceService.getMessage("user.name.invalid"));
        User user = findByNameOrElseThrow(name);
        user.setLastConnectedAt(LocalDateTime.now());
    }
}