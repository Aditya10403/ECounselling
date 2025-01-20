package com.ECounselling.controller;

import com.ECounselling.model.College;
import com.ECounselling.model.Student;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.service.AdminService;
import com.ECounselling.service.CollegeService;
import com.ECounselling.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/college")
    public ResponseEntity<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping("/student")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/college/status/{collegeId}")
    public ResponseEntity<ApiResponse> toggleCollegeStatus(@PathVariable("collegeId") Long cid, @RequestBody College c) {
        ApiResponse response = collegeService.toggleCollegeStatus(cid, c);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginAdmin(@RequestBody Map<String, String> credentials) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");

        try {
            ApiResponse response = adminService.loginAdmin(userId, password);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse(
                            HttpStatus.UNAUTHORIZED.value(),
                            e.getMessage(),
                            null
                    )
            );
        }
    }

}

