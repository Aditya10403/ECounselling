package com.ECounselling.controller;

import com.ECounselling.model.*;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.repository.CounsellingStatusRepository;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.service.AdminService;
import com.ECounselling.service.CollegeService;
import com.ECounselling.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CounsellingStatusRepository counsellingStatusRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(
                "Server is running...",
                HttpStatus.OK
        );
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

    @PostMapping("/applications/save")
    public ResponseEntity<ApiResponse> saveApplication(@RequestBody Application application) {
        try {
            Application savedApplication = applicationRepository.save(application);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            HttpStatus.OK.value(),
                            "Application submitted successfully",
                            savedApplication
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage(),
                            null
                    )
            );
        }

    }

    @PostMapping("/applications/save-all")
    public ResponseEntity<List<Application>> saveAllApplications(@RequestBody List<Application> applications) {
        try {
            List<Application> savedApplications = applicationRepository.saveAll(applications);
            return new ResponseEntity<>(savedApplications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-counselling-status")
    public ResponseEntity<Boolean> checkCounsellingStatus() {
        return ResponseEntity.ok(
                counsellingStatusRepository.findById(1L)
                        .map(CounsellingStatus::isCounsellingStarted)
                        .orElse(false)
        );
    }


}
