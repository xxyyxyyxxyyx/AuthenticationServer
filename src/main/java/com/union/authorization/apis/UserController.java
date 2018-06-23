package com.union.authorization.apis;

import com.union.authorization.model.User;
import com.union.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{staffCode}/password",
            consumes = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<?> updatePassword(@PathVariable Long staffCode, @RequestBody Map<String, String> payload) {
        User user = this.userService.findOne(staffCode);
        user.setPassword(payload.get("password"));
        this.userService.update(staffCode, user);
        return new ResponseEntity<Object>(HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        return new ResponseEntity<Collection<User>>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(
            value = "/{staffCode}")
    public ResponseEntity<User> getUser(@PathVariable("staffCode") Long staffCode) {
        return new ResponseEntity<User>(userService.findOne(staffCode), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map> createUser(@RequestBody User newUser) {
        User user = userService.create(newUser);
        Map<String, String> response = new HashMap<>();
        response.put("staffCode", user.getStaffCode().toString());
        response.put("department", user.getDepartment());
        return new ResponseEntity<Map>(response, HttpStatus.CREATED);
//        return new ResponseEntity<User>(userService.create(newUser), HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/{staffCode}",
            method = RequestMethod.PUT
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable(value = "staffCode") Long staffCode, @RequestBody User updatedUser) {
        return new ResponseEntity<User>(userService.update(staffCode, updatedUser), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{staffCode}",
            method = RequestMethod.DELETE
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable("staffCode") Long staffCode) {
        userService.delete(staffCode);
        return new ResponseEntity<User>(HttpStatus.OK);
    }
}
