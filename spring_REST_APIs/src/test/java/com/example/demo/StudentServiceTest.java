package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.entities.Student;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repos.StdRepo;
import com.example.demo.service.StdService;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StdRepo studentRep;

    @InjectMocks
    private StdService studentService;

    @Test
    void getStudent_WhenStudentExists_ReturnsStudent() {
        Integer regNo = 1;
        Student student = new Student();
        student.setReg_no(regNo);
        student.setName("Karan");

        when(studentRep.findById(regNo)).thenReturn(Optional.of(student));

        Student result = studentService.getStudent(regNo);

        assertNotNull(result);
        assertEquals(regNo, result.getReg_no());
        assertEquals("Karan", result.getName());

        verify(studentRep, times(1)).findById(regNo);
    }

    @Test
    void getStudent_WhenStudentNotFound_ThrowsStudentNotFoundException() {
        Integer regNo = 99;

        when(studentRep.findById(regNo)).thenReturn(Optional.empty());

        StudentNotFoundException ex = assertThrows(
            StudentNotFoundException.class, () -> studentService.getStudent(regNo)
        );

        assertEquals("Student with ID 99 not found", ex.getMessage());
        verify(studentRep, times(1)).findById(regNo);
    }
}