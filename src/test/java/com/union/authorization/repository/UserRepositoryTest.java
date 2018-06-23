package com.union.authorization.repository;

import com.union.authorization.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void testGetAllUsers() {
        User user1 = getUser();
        User user2 = getSecondUser();
        entityManager.persist(user1);
        entityManager.persist(user2);
        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers.size()).isEqualTo(5);

    }

    private User getSecondUser() {
        User user = new User();
        user.setUsername("test2");
        user.setPassword("password");
        user.setDepartment("test");
        user.setRole("ROLE_TEST");
        return user;
    }

    @Test
    public void testSaveUser() {
        User user = getUser();
        User savedUser = entityManager.persist(user);
        User foundUser = userRepository.findOne(savedUser.getStaffCode());
        assertThat(savedUser).isEqualTo(foundUser);

    }

    private User getUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setDepartment("test");
        user.setRole("ROLE_TEST");
        return user;
    }

}