package com.ECounselling.service;

import com.ECounselling.model.Student;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentService {
    ApiResponse addStudentData(Student student);
    List<Student> getAllStudents();
    ApiResponse getStudentDetails(String mailId);
    ApiResponse updateStudentByMail(String mailId, Student s);
    List<Map<String, Object>> getDepartmentsByERank(Integer erank);
    Optional<Student> findByMailId(String mailId);
    Boolean deleteByMailId(String mailId);

    MailResponse forgotPassword(String mailId);
    MailResponse validateOtp(String mailId, String otp);
    MailResponse resetPassword(String mailId, String newPassword);


}
