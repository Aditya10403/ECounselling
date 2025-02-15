package com.ECounselling.service;

import com.ECounselling.exception.CollegeNotFoundException;
import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.repository.CollegeRepository;
import com.ECounselling.repository.DepartmentRepository;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ApiResponse addCollege(College college) {
        if (collegeRepository.existsByCollegeName(college.getCollegeName())) {
            throw new IllegalArgumentException("College with name '" + college.getCollegeName() + "' already exists");
        }
        college.setStatus(true);
        college.setPassword(passwordEncoder.encode(college.getPassword()));
        College savedCollege = collegeRepository.save(college);
        return new ApiResponse(
                HttpStatus.CREATED.value(),
                "College added successfully",
                savedCollege
        );
    }

    @Override
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    @Override
    public Optional<College> findByMailId(String mailId) {
        return collegeRepository.findByMailId(mailId);
    }

    @Override
    public ApiResponse getCollegeDetails(String mailId) {
        College college = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with mailId: " + mailId));
        return new ApiResponse(
                HttpStatus.OK.value(),
                "College retrieved successfully",
                college
        );
    }

    @Override
    public ApiResponse getCollegeDetailsByName(String collegeName) {
        College college = collegeRepository.findByCollegeName(collegeName)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with name: " + collegeName));
        return new ApiResponse(
                HttpStatus.OK.value(),
                "College retrieved successfully",
                college
        );
    }

    @Override
    public ApiResponse updateCollegeByMailId(String mailId, College c) {
        College existingCollege = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with mailId: " + mailId));

        existingCollege.setCollegeName(c.getCollegeName() != null && !c.getCollegeName().isEmpty() ? c.getCollegeName() : existingCollege.getCollegeName());
        existingCollege.setAddress(c.getAddress() != null && !c.getAddress().isEmpty() ? c.getAddress() : existingCollege.getAddress());
        existingCollege.setContactInfo(c.getContactInfo() != null && !c.getContactInfo().isEmpty() ? c.getContactInfo() : existingCollege.getContactInfo());
        existingCollege.setNirfRank(c.getNirfRank() != null ? c.getNirfRank() : existingCollege.getNirfRank());
        existingCollege.setLogo(c.getLogo() != null && !c.getLogo().isEmpty() ? c.getLogo() : existingCollege.getLogo());

        College updatedCollege = collegeRepository.save(existingCollege);
        return new ApiResponse(
                HttpStatus.OK.value(),
                "College updated successfully",
                updatedCollege
        );
    }

    @Override
    public Boolean deleteByMailId(String mailId) {
        Optional<College> collegeInDB = collegeRepository.findByMailId(mailId);
        if (collegeInDB.isPresent()) {
            collegeRepository.delete(collegeInDB.get());
            return true;
        }
        return false;
    }

    @Override
    public ApiResponse toggleCollegeStatus(String collegeName) {
        College college = collegeRepository.findByCollegeName(collegeName)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with name: " + collegeName));
        college.setStatus(!college.getStatus());
        College updatedCollege = collegeRepository.save(college);
        return new ApiResponse(
                HttpStatus.OK.value(),
                college.getStatus() ? "College Unblocked successfully" : "College Blocked successfully",
                updatedCollege
        );
    }

    @Override
    public ApiResponse addDepartmentToCollege(String mailId, Department department) {
        College college = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with mailId: " + mailId));

        department.setCollege(college);
        Department savedDepartment = departmentRepository.save(department);

        return new ApiResponse(
                HttpStatus.CREATED.value(),
                "Department added to college successfully",
                savedDepartment
        );
    }

    @Override
    public ApiResponse getAllDepartmentsOfCollege(String mailId) {
        College college = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new CollegeNotFoundException("College not found with mailId: " + mailId));

        List<Department> departments = college.getDepartments();
        return new ApiResponse(
                HttpStatus.OK.value(),
                "Departments retrieved successfully",
                departments
        );
    }

    @Autowired
    private EmailService emailService;

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    @Override
    public MailResponse forgotPassword(String mailId) {
        College college = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new IllegalArgumentException("College not found with mailId: " + mailId));

        String otp = generateOtp();
        otpStore.put(mailId, otp);

        try {
            emailService.sendOtpEmail(
                    college.getMailId(),
                    "OTP for Password Reset",
                    "Your OTP is: " + otp
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email");
        }
        return new MailResponse(
                HttpStatus.OK.value(),
                "OTP sent successfully to your email"
        );
    }

    @Override
    public MailResponse validateOtp(String mailId, String otp) {
        if (!otpStore.containsKey(mailId) || !otpStore.get(mailId).equals(otp)) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }
        otpStore.remove(mailId);
        return new MailResponse(
                HttpStatus.OK.value(),
                "OTP validated successfully"
        );
    }

    // Returns a 6 digit OTP
    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    @Override
    public MailResponse resetPassword(String mailId, String newPassword) {
        College college = collegeRepository.findByMailId(mailId)
                .orElseThrow(() -> new IllegalArgumentException("College not found with mailId: " + mailId));

        college.setPassword(passwordEncoder.encode(newPassword));
        collegeRepository.save(college);

        return new MailResponse(
                HttpStatus.OK.value(),
                "Password reset successfully"
        );
    }
}
