package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Student;
import com.example.demo.exception.StudentAlreadyExistsException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repos.StdRepo;

@Service
public class StdService {
	@Autowired
	StdRepo studentRep;
	
	public List<Student> getAllStudents() {
		return studentRep.findAll();
	}
	
	public Optional<Student> getStudent(Integer reg_no) {
		if(!studentRep.existsById(reg_no)) {
			throw new StudentNotFoundException(reg_no);
		}
	    return studentRep.findById(reg_no);
	}

	public void saveStudent(Student s) {
	    if (studentRep.existsById(s.getReg_no())) {
	        throw new StudentAlreadyExistsException(s.getReg_no());
	    }
	    studentRep.save(s);
	}

	public void updateStudent(Integer reg_no, Student s ) {
		if(!studentRep.existsById(reg_no)) {
			throw new StudentNotFoundException(reg_no);
		} else {
			s.setReg_no(reg_no);
			studentRep.save(s);
		}
	}
	
	public void deleteStudent(Integer reg_no) {
		if(!studentRep.existsById(reg_no)) {
			throw new StudentNotFoundException(reg_no);
			} else {
				studentRep.deleteById(reg_no);
			}
	}
	
	public void partiallyUpdateStudent(Integer reg_no, Student s) {
	    Optional<Student> student = studentRep.findById(reg_no);

	    if (!student.isPresent()) {
	    	throw new StudentNotFoundException(reg_no);
	    }

	    Student st = student.get();

	    if (s.getName() != null) st.setName(s.getName());
	    if (s.getRoll_no() != null) st.setRoll_no(s.getRoll_no());
	    if (s.getStandard() != null) st.setStandard(s.getStandard());
	    if (s.getSchool() != null) st.setSchool(s.getSchool());
	    if (s.getGender() != null) st.setGender(s.getGender());
	    if (s.getPercentage() != null) st.setPercentage(s.getPercentage());

	    studentRep.save(st);
	}

}

