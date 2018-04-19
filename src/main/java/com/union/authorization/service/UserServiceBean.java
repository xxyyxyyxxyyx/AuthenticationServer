package com.union.authorization.service;

import com.union.authorization.model.User;
import com.union.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceBean implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserServiceBean(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findByUsernameAndDepartment(String username, String department) {
        return userRepository.findByUsernameAndDepartment(username, department);
    }

    @Override
    public User findOne(Long staffCode) {
        return userRepository.findOne(staffCode);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User newUser) {
        newUser.setPassword("");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole("ROLE_USER");
        return userRepository.save(newUser);
    }

    @Override
    public User update(Long staffCode, User updatedUser) {
        User user = userRepository.findOne(staffCode);
        assert user != null;
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setUsername(updatedUser.getUsername());
        User changedUser = userRepository.save(user);
        return changedUser;
    }

    @Override
    public void delete(Long staffCode) {
        userRepository.delete(staffCode);
    }
}
