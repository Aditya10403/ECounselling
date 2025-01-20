package com.ECounselling.controller;

import com.ECounselling.model.College;
import com.ECounselling.model.Student;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.service.CollegeService;
import com.ECounselling.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeService collegeService;

    @GetMapping("/college")
    public ResponseEntity<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping("/student")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/college/status/{collegeId}")
    public ResponseEntity<ApiResponse> toggleCollegeStatus(@PathVariable("collegeId") Long cid, @RequestBody College c) {
        ApiResponse response = collegeService.toggleCollegeStatus(cid, c);
        return ResponseEntity.ok(response);
    }
}

