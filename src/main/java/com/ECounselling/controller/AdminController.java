package com.ECounselling.controller;

import com.ECounselling.model.Admin;
import com.ECounselling.model.College;
import com.ECounselling.model.Student;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.service.AdminService;
import com.ECounselling.service.CollegeService;
import com.ECounselling.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody Admin admin) {
        try {
            ApiResponse response = adminService.addAdmin(admin);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage(),
                            null
                    )
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        Optional<Admin> admin = adminService.findByMailId(mailId);
        return admin.isPresent() ?
                new ResponseEntity<>(admin, HttpStatus.OK) :
                new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/all-college")
    public ResponseEntity<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        if (colleges != null && !colleges.isEmpty()) {
            return new ResponseEntity<>(colleges, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all-student")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students != null && !students.isEmpty()) {
            return new ResponseEntity<>(students, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/toggle-status/{collegeName}")
    public ResponseEntity<ApiResponse> toggleCollegeStatus(@PathVariable("collegeName") String collegeName) {
        ApiResponse response = collegeService.toggleCollegeStatus(collegeName);
        return ResponseEntity.ok(response);
    }

}

