package com.ECounselling.model;

import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentId;

    private String studentName;
    private String contactNumber;
    private String address;
    private String tenthboard;
    private String twelfthboard;
    private String schoolName;
    private String mailId;
    private Double tenthMarks;
    private Double twelveMarks;
    private String img;
    private Integer erank;
    private String password;
    private String role = "STUDENT";

    public Student() { }

    public Student(Long studentId, String studentName, String contactNumber, String address, String tenthboard, String twelfthboard, String schoolName, String mailId, Double tenthMarks, Double twelveMarks, String img, Integer erank, String password) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.tenthboard = tenthboard;
        this.twelfthboard = twelfthboard;
        this.schoolName = schoolName;
        this.mailId = mailId;
        this.tenthMarks = tenthMarks;
        this.twelveMarks = twelveMarks;
        this.img = img;
        this.erank = erank;
        this.password = password;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTenthboard() {
        return tenthboard;
    }

    public void setTenthboard(String tenthboard) {
        this.tenthboard = tenthboard;
    }

    public String getTwelfthboard() {
        return twelfthboard;
    }

    public void setTwelfthboard(String twelfthboard) {
        this.twelfthboard = twelfthboard;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public Double getTenthMarks() {
        return tenthMarks;
    }

    public void setTenthMarks(Double tenthMarks) {
        this.tenthMarks = tenthMarks;
    }

    public Double getTwelveMarks() {
        return twelveMarks;
    }

    public void setTwelveMarks(Double twelveMarks) {
        this.twelveMarks = twelveMarks;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getErank() {
        return erank;
    }

    public void setErank(Integer erank) {
        this.erank = erank;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
