package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Gender;
import com.example.demo.entities.Student;
import com.example.demo.repos.StdRepo;
import com.example.demo.service.StdService;

@RestController
public class StudentController {
	
	@Autowired
	private StdRepo stdRepo;
	
	@Autowired
	private StdService stdService;
	
	@GetMapping("/students")
	public ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity.ok(stdService.getAllStudents());
    }
	
	@GetMapping("/students/{reg_no}")
	public ResponseEntity<Student> getById(@PathVariable Integer reg_no) {
        Student student = stdService.getStudent(reg_no);
        return ResponseEntity.ok(student);
    }
	
	@PostMapping("/students")
	public ResponseEntity<String> saveStudent(@RequestBody Student s) {
        stdService.saveStudent(s);
        return ResponseEntity.status(201).body("Student Created!");
    }
	
	@PutMapping("/students/{reg_no}")
	public ResponseEntity<String> updateStudent(@PathVariable Integer reg_no, @RequestBody Student s) {
		stdService.updateStudent(reg_no, s);
		return ResponseEntity.status(200).body("Student Updated!");
	}
	
	@PatchMapping("/students/{reg_no}")
	public ResponseEntity<String> partialUpdate(@PathVariable Integer reg_no, @RequestBody Student s) {
		stdService.partiallyUpdateStudent(reg_no, s);
		return ResponseEntity.status(200).body("Student Partially Updated!");
	}
	
	@DeleteMapping("/students/{reg_no}")
	public ResponseEntity<String> deleteStudent(@PathVariable Integer reg_no) {
		stdService.deleteStudent(reg_no);
		return ResponseEntity.status(200).body("Student Deleted!");
	}
	
	@GetMapping("/students/school")
	public ResponseEntity<List<Student>> getBySchool(@RequestParam String name){
		return ResponseEntity.status(200).body(stdRepo.findBySchool(name));
	}
	
	@GetMapping("/students/school/count")
	public ResponseEntity<Integer> getCountBySchool(@RequestParam String name){
		return ResponseEntity.status(200).body(stdRepo.countBySchool(name));
	}
	
	@GetMapping("/students/school/standard/count")
	public ResponseEntity<Integer> getCountBySchool(@RequestParam Integer standard){
		return ResponseEntity.status(200).body(stdRepo.countByStandard(standard));
	}
	
	@GetMapping("/students/strength")
	public ResponseEntity<Integer> getStrength(@RequestParam Gender gender, Integer standard){
		return ResponseEntity.status(200).body(stdRepo.countByGenderAndStandard(gender, standard));
	}
	
	@GetMapping("students/result")
    public ResponseEntity<List<Student>> getResult(@RequestParam boolean pass) {
        if(pass)
        	return ResponseEntity.status(200).body(stdRepo.findByPercentageGreaterThanEqualOrderByPercentageDesc(40.0));
        else
        	return ResponseEntity.status(200).body(stdRepo.findByPercentageLessThanOrderByPercentageDesc(40.0));
        
    }
	
	
	
}
