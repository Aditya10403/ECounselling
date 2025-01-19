package com.ECounselling.service;

import com.ECounselling.model.Student;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;

import java.util.List;
import java.util.Map;

public interface StudentService {
    ApiResponse addStudentData(Student student);
    List<Student> getAllStudents();
    ApiResponse getStudentById(Long id);
    ApiResponse updateStudentByMail(String mailId, Student s);
    ApiResponse verifyMail(String mailId);
    List<Map<String, Object>> getDepartmentsByERank(Integer erank);
    ApiResponse loginStudent(String mailId, String password);

    MailResponse forgotPassword(String mailId);
    MailResponse validateOtp(String mailId, String otp);
    MailResponse resetPassword(String mailId, String newPassword);

}
