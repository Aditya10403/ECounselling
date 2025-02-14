package com.ECounselling.service;

import com.ECounselling.model.Admin;
import com.ECounselling.model.College;
import com.ECounselling.model.Role;
import com.ECounselling.model.Student;
import com.ECounselling.repository.AdminRepository;
import com.ECounselling.repository.CollegeRepository;
import com.ECounselling.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (collegeRepository.findByMailId(email).isPresent()) {
            College college = collegeRepository.findByMailId(email).get();
            return createUser(college.getMailId(), college.getPassword(), college.getRole());
        } else if (studentRepository.findByMailId(email).isPresent()) {
            Student student = studentRepository.findByMailId(email).get();
            return createUser(student.getMailId(), student.getPassword(), student.getRole());
        } else if (adminRepository.findByMailId(email).isPresent()) {
            Admin admin = adminRepository.findByMailId(email).get();
            return createUser(admin.getMailId(), admin.getPassword(), admin.getRole());
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails createUser(String username, String password, Role role) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .roles(role.name())
                .build();
    }
}

