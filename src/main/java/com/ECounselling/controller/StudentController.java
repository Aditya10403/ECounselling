package com.ECounselling.controller;

import com.ECounselling.model.*;
import com.ECounselling.repository.AllocationResultRepository;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.response.MailResponse;
import com.ECounselling.service.AllocationResultService;
import com.ECounselling.service.ApplicationService;
import com.ECounselling.service.CounsellingStatusService;
import com.ECounselling.service.StudentService;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AllocationResultService allocationResultService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/get-details")
    public ResponseEntity<?> getStudentDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        ApiResponse response = studentService.getStudentDetails(mailId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateStudentByMail(@RequestBody Student student) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        ApiResponse response = studentService.updateStudentByMail(mailId, student);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/departments/by-erank/{erank}")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentsByERank(@PathVariable("erank") Integer erank) {
        List<Map<String, Object>> departmentsWithCollege = studentService.getDepartmentsByERank(erank);
        return ResponseEntity.ok(departmentsWithCollege);
    }

    @GetMapping("/{studentName}/result")
    public ResponseEntity<ApiResponse> getResultsByStudentName(@PathVariable String studentName) {
        ApiResponse response = allocationResultService.getResultsByStudentName(studentName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
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

    @GetMapping("/applications/{studentName}")
    public ResponseEntity<ApiResponse> getApplicationByStudentName(@PathVariable String studentName) {
        ApiResponse response = applicationService.getApplicationByStudentName(studentName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByMailId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mailId = authentication.getName();
        Boolean studentInDB = studentService.deleteByMailId(mailId);
        return studentInDB ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MailResponse> forgotPassword(@RequestBody Map<String, String> request) {
        String mailId = request.get("mailId");

        try {
            MailResponse response = studentService.forgotPassword(mailId);
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
            MailResponse response = studentService.validateOtp(mailId, otp);
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
            MailResponse response = studentService.resetPassword(mailId, newPassword);
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
