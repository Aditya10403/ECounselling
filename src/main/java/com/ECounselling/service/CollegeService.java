package com.ECounselling.service;

import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;

import java.util.List;
import java.util.Optional;

public interface CollegeService {
    ApiResponse addCollege(College c);
    List<College> getAllColleges();
    ApiResponse updateCollegeByMailId(String mailId, College c);
    ApiResponse getCollegeDetails(String mailId);
    ApiResponse getCollegeDetailsByName(String collegeName);
    ApiResponse toggleCollegeStatus(String collegeName);
    ApiResponse addDepartmentToCollege(String mailId, Department department);
    ApiResponse getAllDepartmentsOfCollege(String mailId);
    Optional<College> findByMailId(String mailId);
    Boolean deleteByMailId(String mailId);

    MailResponse forgotPassword(String mailId);
    MailResponse validateOtp(String mailId, String otp);
    MailResponse resetPassword(String mailId, String newPassword);
}
