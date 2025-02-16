package com.ECounselling.controller;

import com.ECounselling.model.Application;
import com.ECounselling.model.Student;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping("/save")
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

    @PostMapping("/save-all")
    public ResponseEntity<List<Application>> saveAllApplications(@RequestBody List<Application> applications) {
        try {
            List<Application> savedApplications = applicationRepository.saveAll(applications);
            return new ResponseEntity<>(savedApplications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}