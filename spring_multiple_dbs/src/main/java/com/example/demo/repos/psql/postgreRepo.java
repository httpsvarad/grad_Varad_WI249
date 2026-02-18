package com.example.demo.repos.psql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Student;

public interface postgreRepo extends JpaRepository<Student, Integer> {

}
