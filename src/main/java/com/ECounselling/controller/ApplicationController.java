package com.ECounselling.controller;

import com.ECounselling.model.Application;
import com.ECounselling.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping("/save")
    public ResponseEntity<Application> saveApplication(@RequestBody Application application) {
        Application savedApplication = applicationRepository.save(application);
        return ResponseEntity.ok(savedApplication);
    }

    @PostMapping("/save-all")
    public ResponseEntity<List<Application>> saveAllApplications(@RequestBody List<Application> applications) {
        List<Application> savedApplications = applicationRepository.saveAll(applications);
        return ResponseEntity.ok(savedApplications);
    }
}