package com.ECounselling.controller;

import com.ECounselling.exception.CollegeNotFoundException;
import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;
import com.ECounselling.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @PostMapping("/add")
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

    @GetMapping("/all")
    public ResponseEntity<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping("/find/{collegeId}")
    public ResponseEntity<ApiResponse> getCollegeById(@PathVariable("collegeId") Long collegeId) {
        ApiResponse response = collegeService.getCollegeById(collegeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{collegeId}")
    public ResponseEntity<ApiResponse> updateCollegeById(
            @PathVariable("collegeId") Long collegeId,
            @RequestBody College college) {
        ApiResponse response = collegeService.updateCollegeById(collegeId, college);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/findByName/{collegeName}")
    public ResponseEntity<ApiResponse> getCollegeByName(@PathVariable("collegeName") String collegeName) {
        ApiResponse response = collegeService.getCollegeByName(collegeName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{collegeId}")
    public ResponseEntity<ApiResponse> deleteCollegeById(@PathVariable("collegeId") Long collegeId) {
        ApiResponse response = collegeService.deleteCollegeById(collegeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{collegeId}/department/add")
    public ResponseEntity<ApiResponse> addDepartmentToCollege(@PathVariable Long collegeId, @RequestBody Department department) {
        try {
            ApiResponse response = collegeService.addDepartmentToCollege(collegeId, department);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (CollegeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null)
            );
        }
    }

    @GetMapping("/{collegeId}/departments")
    public ResponseEntity<ApiResponse> getAllDepartmentsByCollege(@PathVariable Long collegeId) {
        try {
            ApiResponse response = collegeService.getAllDepartmentsByCollege(collegeId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (CollegeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null)
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginStudent(@RequestBody Map<String, String> credentials) {
        String mailId = credentials.get("mailId");
        String password = credentials.get("password");

        try {
            ApiResponse response = collegeService.loginCollege(mailId, password);
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

    @PostMapping("/forgot-password")
    public ResponseEntity<MailResponse> forgotPassword(@RequestBody Map<String, String> request) {
        String mailId = request.get("mailId");

        try {
            MailResponse response = collegeService.forgotPassword(mailId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new MailResponse(
                            HttpStatus.NOT_FOUND.value(),
                            e.getMessage()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MailResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<MailResponse> validateOtp(@RequestBody Map<String, String> request) {
        String mailId = request.get("mailId");
        String otp = request.get("otp");

        try {
            MailResponse response = collegeService.validateOtp(mailId, otp);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MailResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MailResponse> resetPassword(@RequestBody Map<String, String> request) {
        String mailId = request.get("mailId");
        String newPassword = request.get("newPassword");

        try {
            MailResponse response = collegeService.resetPassword(mailId, newPassword);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MailResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    )
            );
        }
    }
}
