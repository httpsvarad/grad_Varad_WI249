package com.example.demo.entities;
import jakarta.persistence.Entity;

import jakarta.persistence.*;

@Entity
@Table
public class Student {
	@Id
	private Integer reg_no;
	private Integer roll_no;
	private String name;
	private Integer standard;
	private String school;
	
	@Enumerated(EnumType.STRING)
    private Gender gender;
	
	private Double percentage;

	public Integer getReg_no() {
		return reg_no;
	}

	public void setReg_no(Integer reg_no) {
		this.reg_no = reg_no;
	}

	public Integer getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(Integer roll_no) {
		this.roll_no = roll_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStandard() {
		return standard;
	}

	public void setStandard(Integer standard) {
		this.standard = standard;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	
}
