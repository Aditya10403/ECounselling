package com.ECounselling.service;

import com.ECounselling.exception.StudentNotFoundException;
import com.ECounselling.model.College;
import com.ECounselling.model.Department;
import com.ECounselling.model.Student;
import com.ECounselling.repository.DepartmentRepository;
import com.ECounselling.repository.StudentRepository;
import com.ECounselling.response.ApiResponse;
import com.ECounselling.response.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ApiResponse addStudentData(Student student) {
        if (studentRepository.existsByMailId(student.getMailId())) {
            throw new IllegalArgumentException("Student with mailId '" + student.getMailId() + "' already exists");
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Student savedStudent = studentRepository.save(student);
        return new ApiResponse(
                HttpStatus.CREATED.value(),
                "Student added successfully",
                savedStudent
        );
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public ApiResponse getStudentDetails(String mailId) {
        Student student = studentRepository.findByMailId(mailId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with mailId: " + mailId));
        return new ApiResponse(
                HttpStatus.OK.value(),
                "Student retrieved successfully",
                student
        );
    }

    @Override
    public ApiResponse updateStudentByMail(String mailId, Student student) {
        Student existingStudent = studentRepository.findByMailId(mailId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with mailId: " + mailId));
        updateStudentFields(existingStudent, student);
        Student updatedStudent = studentRepository.save(existingStudent);
        return new ApiResponse(
                HttpStatus.OK.value(),
                "Student updated successfully",
                updatedStudent
        );
    }


    @Override
    public List<Map<String, Object>> getDepartmentsByERank(Integer erank) {
        List<Department> departments = departmentRepository.findDepartmentsByCutoffRank(erank);

        List<Map<String, Object>> response = new ArrayList<>();
        for (Department department : departments) {
            Map<String, Object> map = new HashMap<>();
            map.put("departmentId", department.getDepartmentId());
            map.put("departmentName", department.getDepartmentName());
            map.put("noOfSeats", department.getNoOfSeats());
            map.put("cutoffRank", department.getCutoffRank());

            College college = department.getCollege();
            map.put("collegeId", college.getCollegeId());
            map.put("collegeName", college.getCollegeName());
            map.put("collegeAddress", college.getAddress());

            response.add(map);
        }

        return response;
    }

    @Override
    public Optional<Student> findByMailId(String mailId) {
        return studentRepository.findByMailId(mailId);
    }

    private void updateStudentFields(Student existingStudent, Student student) {
        existingStudent.setStudentName(student.getStudentName() != null && !student.getStudentName().isEmpty() ? student.getStudentName() : existingStudent.getStudentName());
        existingStudent.setContactNumber(student.getContactNumber() != null && !student.getContactNumber().isEmpty() ? student.getContactNumber() : existingStudent.getContactNumber());
        existingStudent.setAddress(student.getAddress() != null && !student.getAddress().isEmpty() ? student.getAddress() : existingStudent.getAddress());
        existingStudent.setTenthboard(student.getTenthboard() != null && !student.getTenthboard().isEmpty() ? student.getTenthboard() : existingStudent.getTenthboard());
        existingStudent.setTwelfthboard(student.getTwelfthboard() != null && !student.getTwelfthboard().isEmpty() ? student.getTwelfthboard() : existingStudent.getTwelfthboard());
        existingStudent.setSchoolName(student.getSchoolName() != null && !student.getSchoolName().isEmpty() ? student.getSchoolName() : existingStudent.getSchoolName());
        existingStudent.setTenthMarks(student.getTenthMarks() != null ? student.getTenthMarks() : existingStudent.getTenthMarks());
        existingStudent.setTwelveMarks(student.getTwelveMarks() != null ? student.getTwelveMarks() : existingStudent.getTwelveMarks());
        existingStudent.setImg(student.getImg() != null && !student.getImg().isEmpty() ? student.getImg() : existingStudent.getImg());
        existingStudent.setErank(student.getErank() != null ? student.getErank() : existingStudent.getErank());
    }

    @Override
    public Boolean deleteByMailId(String mailId) {
        Optional<Student> studentInDB = studentRepository.findByMailId(mailId);
        if (studentInDB.isPresent()) {
            studentRepository.delete(studentInDB.get());
            return true;
        }
        return false;
    }


    @Autowired
    private EmailService emailService;

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    @Override
    public MailResponse forgotPassword(String mailId) {
        Student student = studentRepository.findByMailId(mailId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with mailId: " + mailId));

        String otp = generateOtp();
        otpStore.put(mailId, otp);

        try {
            emailService.sendOtpEmail(
                    student.getMailId(),
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
        Student student = studentRepository.findByMailId(mailId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with mailId: " + mailId));

        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);

        return new MailResponse(
                HttpStatus.OK.value(),
                "Password reset successfully"
        );
    }

}
