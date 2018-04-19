package com.union.authorization.repository;

import com.union.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndDepartment(String username, String department);
}
