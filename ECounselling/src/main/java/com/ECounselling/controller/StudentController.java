package com.ECounselling.controller;

import com.ECounselling.model.Student;
import com.ECounselling.response.MailResponse;
import com.ECounselling.service.StudentService;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginStudent(@RequestBody Map<String, String> credentials) {
        String mailId = credentials.get("mailId");
        String password = credentials.get("password");

        try {
            ApiResponse response = studentService.loginStudent(mailId, password);
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

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable("id") Long id) {
        ApiResponse response = studentService.getStudentById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{mailId}")
    public ResponseEntity<ApiResponse> updateStudentByMail(@PathVariable("mailId") String mailId, @RequestBody Student student) {
        ApiResponse response = studentService.updateStudentByMail(mailId, student);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/verify/mail/{mailId}")
    public ResponseEntity<ApiResponse> verifyMail(@PathVariable("mailId") String mailId) {
        ApiResponse response = studentService.verifyMail(mailId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/departments/by-erank/{erank}")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentsByERank(@PathVariable("erank") Integer erank) {
        List<Map<String, Object>> departmentsWithCollege = studentService.getDepartmentsByERank(erank);
        return ResponseEntity.ok(departmentsWithCollege);
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
