package com.union.authorization.service;

import com.union.authorization.model.User;
import com.union.authorization.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceBeanTest {
    @TestConfiguration
    static class UserServiceBeanTestContextConfiguration {
        @Bean
        public UserServiceBean userServiceBean() {
            return new UserServiceBean();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserServiceBean userServiceBean;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testFindByUsernameAndDepartment() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setRole("ROLE_TEST");
        user.setDepartment("test");

        Mockito.when(userRepository.findByUsernameAndDepartment(user.getUsername(), user.getDepartment())).thenReturn(user);
        assertThat(userServiceBean.findByUsernameAndDepartment(user.getUsername(), user.getDepartment())).isEqualTo(user);
    }

    @Test
    public void testFindOne() {
        User user = new User();
        user.setStaffCode((long) 1);
        user.setUsername("test");
        user.setPassword("password");
        user.setDepartment("test");
        Mockito.when(userRepository.findOne((long) 1)).thenReturn(user);
        assertThat(userServiceBean.findOne((long) 1)).isEqualTo(user);


    }
}