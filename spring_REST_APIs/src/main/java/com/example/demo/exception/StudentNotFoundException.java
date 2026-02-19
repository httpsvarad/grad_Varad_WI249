package com.example.demo.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Integer regNo) {
        super("Student with ID " + regNo + " not found");
    }
}