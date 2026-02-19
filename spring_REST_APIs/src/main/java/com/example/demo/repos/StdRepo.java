package com.example.demo.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Gender;
import com.example.demo.entities.Student;

public interface StdRepo extends JpaRepository<Student, Integer> {
	public List<Student> findBySchool(String school);
	
	public Integer countBySchool(String school);
	
	public Integer countByStandard(Integer standard);
	
	public Integer countByGenderAndStandard(Gender gender, Integer standard);
	
	public List<Student> findByPercentageGreaterThanEqualOrderByPercentageDesc(Double percentage);
	
	public List<Student> findByPercentageLessThanOrderByPercentageDesc(Double percentage);
}