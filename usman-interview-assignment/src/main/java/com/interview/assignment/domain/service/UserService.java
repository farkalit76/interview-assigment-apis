package com.interview.assignment.domain.service;

import com.interview.assignment.repository.UserRepository;
import com.interview.assignment.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getByUerId(Long userId) {
        return userRepository.findById(userId);
    }

}
