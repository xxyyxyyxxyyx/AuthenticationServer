package com.union.authorization.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username", "department"})
)

public class User {

    @Id
    @Column(name = "staff_code")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long staffCode;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "department")
    @NotNull
    private String department;

    @Column(name = "role")
    @NotNull
    private String role;

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(Long staffCode) {
        this.staffCode = staffCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
