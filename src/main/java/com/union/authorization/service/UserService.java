package com.union.authorization.service;

import com.union.authorization.model.User;

import java.util.Collection;

public interface UserService {
    User findByUsernameAndDepartment(String username, String department);

    User findOne(Long staffCode);

    Collection<User> findAll();

    User create(User newUser);

    User update(Long staffCode, User updatedUser);

    void delete(Long staffCode);
}
