package com.ECounselling.service;

import com.ECounselling.model.Admin;
import com.ECounselling.repository.AdminRepository;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ApiResponse addAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepository.save(admin);
        return new ApiResponse(
                HttpStatus.CREATED.value(),
                "Admin created successfully",
                savedAdmin
        );
    }

    public Optional<Admin> findByMailId(String mailId) {
        return adminRepository.findByMailId(mailId);
    }
}

