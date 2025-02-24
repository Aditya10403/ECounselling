package com.ECounselling.controller;

import com.ECounselling.model.*;
import com.ECounselling.repository.AllocationResultRepository;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.repository.CounsellingStatusRepository;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CounsellingStatusService counsellingStatusService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AllocationResultRepository allocationResultRepository;

    @PostMapping("/signup")
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

    @GetMapping("student-details/{studentName}")
    public ResponseEntity<ApiResponse> getStudentDetailsByName(@PathVariable String studentName) {
        ApiResponse response = studentService.getStudentDetailsByName(studentName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("college-details/{collegeName}")
    public ResponseEntity<ApiResponse> getCollegeDetailsByName(@PathVariable String collegeName) {
        ApiResponse response = collegeService.getCollegeDetailsByName(collegeName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
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

    @GetMapping("/all-applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        if (applications != null && !applications.isEmpty()) {
            return new ResponseEntity<>(applications, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allocation-result")
    public ResponseEntity<List<AllocationResult>> getAllAllocationResults() {
        List<AllocationResult> results = allocationResultRepository.findAll();
        if (results != null && !results.isEmpty()) {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/toggle-status/{collegeName}")
    public ResponseEntity<ApiResponse> toggleCollegeStatus(@PathVariable("collegeName") String collegeName) {
        ApiResponse response = collegeService.toggleCollegeStatus(collegeName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/give-result")
    public ResponseEntity<List<AllocationResult>> getAllocationResults() {
        List<AllocationResult> allocationResults = applicationService.allocateDepartments();
        if (allocationResults != null && !allocationResults.isEmpty()) {
            return new ResponseEntity<>(allocationResults, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/set-status/{status}")
    public ResponseEntity<ApiResponse> setCounsellingStatus(@PathVariable Status status) {
        CounsellingStatus updatedStatus = counsellingStatusService.updateStatus(status);
        return ResponseEntity.ok(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Counselling status updated successfully",
                        updatedStatus
                )
        );
    }

    @DeleteMapping("/reset-allotment")
    public ResponseEntity<ApiResponse> resetAllotmentProcess() {
        Boolean resetSuccess = applicationService.resetAllotmentProcess();
        counsellingStatusService.updateStatus(Status.NOT_STARTED);
        return resetSuccess ?
                ResponseEntity.ok(
                        new ApiResponse(
                                HttpStatus.OK.value(),
                                "Reset Success",
                                null
                        )
                ) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse(
                             HttpStatus.BAD_REQUEST.value(),
                             "Reset Failed",
                             null
                        )
        );
    }

}

