package com.union.authorization.security;

import com.union.authorization.model.User;
import com.union.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomDetailsService implements UserDetailsService {
    private UserService userService;

    private HttpServletRequest request;

    @Autowired
    public CustomDetailsService(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String department = request.getParameter("department");
        User user = userService.findByUsernameAndDepartment(username, department);
        // Creating an empty Authorities list
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(
                new SimpleGrantedAuthority(user.getRole())
        );
        // Creating the spring security user
        org.springframework.security.core.userdetails.User springSecurityUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        return springSecurityUser;
    }
}
