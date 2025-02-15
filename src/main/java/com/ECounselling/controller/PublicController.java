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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(
                "Server is running...",
                HttpStatus.OK
        );
    }

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

    @PostMapping("/create-student")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody Student student) {
        try {
            ApiResponse response = studentService.addStudentData(student);
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

    @PostMapping("/create-college")
    public ResponseEntity<ApiResponse> addCollege(@RequestBody College college) {
        try {
            ApiResponse response = collegeService.addCollege(college);
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
}
