package com.union.authorization.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    private Long staffCode;
    private String departmentID;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long staffCode, String departmentID) {
        super(username, password, authorities);
        this.staffCode = staffCode;
        this.departmentID = departmentID;
    }

    public Long getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(Long staffCode) {
        this.staffCode = staffCode;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }
}
