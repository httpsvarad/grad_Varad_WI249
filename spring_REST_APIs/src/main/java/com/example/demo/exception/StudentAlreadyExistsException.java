package com.example.demo.exception;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(Integer regNo) {
        super("Student with ID " + regNo + " already exists");
    }
}
