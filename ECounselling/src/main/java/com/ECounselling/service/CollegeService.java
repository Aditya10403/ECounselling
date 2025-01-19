package com.ECounselling.service;

import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;

import java.util.List;

public interface CollegeService {
    ApiResponse addCollege(College c);
    List<College> getAllColleges();
    ApiResponse getCollegeById(Long collegeId);
    ApiResponse updateCollegeById(Long collegeId, College c);
    ApiResponse getCollegeByName(String collegeName);
    ApiResponse deleteCollegeById(Long collegeId);
    ApiResponse toggleCollegeStatus(Long collegeId, College c);
    ApiResponse addDepartmentToCollege(Long collegeId, Department department);
    ApiResponse getAllDepartmentsByCollege(Long collegeId);
    ApiResponse loginCollege(String mailId, String password);

    MailResponse forgotPassword(String mailId);
    MailResponse validateOtp(String mailId, String otp);
    MailResponse resetPassword(String mailId, String newPassword);
}
