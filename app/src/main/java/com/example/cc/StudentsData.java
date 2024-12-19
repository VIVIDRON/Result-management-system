package com.example.cc;

public class StudentsData {

    private String rollNo; // Key for each student
    private String CGPA;   // CGPA value
    private String studentName;
    private String SemName;

    // Default constructor required for Firebase
    public StudentsData() {}

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Constructor with parameters
    public StudentsData(String rollNo, String CGPA, String studentName, String SemName) {
        this.rollNo = rollNo;
        this.CGPA = CGPA;
        this.studentName = studentName;
        this.SemName = SemName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getSemName() {
        return SemName;
    }

    public String getCGPA() {
        return CGPA;
    }
}