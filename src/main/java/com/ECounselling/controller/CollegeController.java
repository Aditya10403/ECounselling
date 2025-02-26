package com.ECounselling.controller;

import com.ECounselling.exception.CollegeNotFoundException;
import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;
import com.ECounselling.service.AllocationResultService;
import com.ECounselling.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AllocationResultService allocationResultService;

    @GetMapping("get-details")
    public ResponseEntity<ApiResponse> getCollegeDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        ApiResponse response = collegeService.getCollegeDetails(mailId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get-details/{collegeName}")
    public ResponseEntity<ApiResponse> getCollegeDetailsByName(@PathVariable String collegeName) {
        ApiResponse response = collegeService.getCollegeDetailsByName(collegeName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get-details/{collegeName}/{departmentName}")
    public ResponseEntity<ApiResponse> getDepartmentDetailsByName(
            @PathVariable String collegeName,
            @PathVariable String departmentName) {
        ApiResponse response = collegeService.getDepartmentDetailsByName(collegeName, departmentName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCollegeById(@RequestBody College college) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        ApiResponse response = collegeService.updateCollegeByMailId(mailId, college);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByMailId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        Boolean collegeInDB = collegeService.deleteByMailId(mailId);
        return collegeInDB ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/add-department")
    public ResponseEntity<ApiResponse> addDepartmentToCollege(@RequestBody Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        try {
            ApiResponse response = collegeService.addDepartmentToCollege(mailId, department);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (CollegeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null)
            );
        }
    }

    @GetMapping("/all-departments")
    public ResponseEntity<ApiResponse> getAllDepartmentsOfCollege() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        try {
            ApiResponse response = collegeService.getAllDepartmentsOfCollege(mailId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (CollegeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null)
            );
        }
    }

    @GetMapping("/{collegeName}/students")
    public ResponseEntity<ApiResponse> getCollegeAllocatedStudents(@PathVariable String collegeName) {
        ApiResponse response = allocationResultService.getResultsByCollegeName(collegeName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
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
