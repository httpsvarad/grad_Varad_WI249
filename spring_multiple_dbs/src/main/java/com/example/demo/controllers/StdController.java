package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.entities.Student;
import com.example.demo.service.StudentService;

@Controller
public class StdController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String std() {
        return "stdpage";   
    }
    
    @PostMapping("/save")
    public String saveData(
            @RequestParam("roll_id") int rollId,
            @RequestParam("name") String name,
            @RequestParam("std") int standard,
            @RequestParam("school") String school
    ) {
        Student s = new Student();
        s.setRoll_id(rollId);
        s.setName(name);
        s.setStandard(standard);
        s.setSchool(school);

        try {
            studentService.saveStudent(s);
        } catch (Exception e) {
            System.out.println("Rollback happened: " + e.getMessage());
        }

        return "redirect:/";   
    }
}

