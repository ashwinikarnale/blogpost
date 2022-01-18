package com.ash.project.blogpost.service;

import com.ash.project.blogpost.model.User;
import com.ash.project.blogpost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAllUsers(String s) {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }
}
