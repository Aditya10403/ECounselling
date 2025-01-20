package com.ECounselling.service;

import com.ECounselling.model.Admin;
import com.ECounselling.repository.AdminRepository;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public ApiResponse loginAdmin(String userId, String password) {
        Optional<Admin> adminOptional = adminRepository.findByUserId(userId);

        if (adminOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid User ID.");
        }

        Admin admin = adminOptional.get();
        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid Password.");
        }

        return new ApiResponse(
                HttpStatus.OK.value(),
                "Login Successful",
                admin
        );
    }
}

