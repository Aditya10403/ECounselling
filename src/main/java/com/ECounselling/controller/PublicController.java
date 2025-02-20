package com.ECounselling.controller;

import com.ECounselling.model.*;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.repository.CounsellingStatusRepository;
import com.ECounselling.response.ApiError;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.AuthRequest;
import com.ECounselling.response.JwtResponse;
import com.ECounselling.service.AdminService;
import com.ECounselling.service.CollegeService;
import com.ECounselling.service.StudentService;
import com.ECounselling.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(
                "Server is running...",
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getMailId(),
                            authRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authRequest.getMailId());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiError(
                            HttpStatus.NOT_FOUND.value(),
                            "Invalid mailId or password"
                    )
            );
        }
    }

    @PostMapping("/signup-student")
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

    @PostMapping("/signup-college")
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
