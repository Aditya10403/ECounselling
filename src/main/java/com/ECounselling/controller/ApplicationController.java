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
        return ResponseEntity.ok(applicationRepository.save(application));
    }

    @PostMapping("/save-all")
    public ResponseEntity<List<Application>> saveAllApplications(@RequestBody List<Application> applications) {
        return ResponseEntity.ok(applicationRepository.saveAll(applications));
    }
}