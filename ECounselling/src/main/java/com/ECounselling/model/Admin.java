package com.ECounselling.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Admin")
public class Admin {

    @Id
    private String userId;

    @Column(nullable = false)
    private String password;

    private String role = "ADMIN";

    public Admin() {}

    public Admin(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

