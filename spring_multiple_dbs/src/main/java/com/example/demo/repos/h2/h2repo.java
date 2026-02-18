package com.example.demo.repos.h2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Student;

public interface h2repo extends JpaRepository <Student, Integer> {

}
