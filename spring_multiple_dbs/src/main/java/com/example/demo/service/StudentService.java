package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Student;
import com.example.demo.repos.h2.h2repo;
import com.example.demo.repos.psql.postgreRepo;
import io.github.httpsvarad.support.exception.ApiException;
import io.github.httpsvarad.support.exception.ErrorCode;
import jakarta.transaction.Transactional;

@Service
public class StudentService {

    @Autowired
    private h2repo h2rep;

    @Autowired
    private postgreRepo postgreRep;

    @Transactional
    public void saveStudent(Student s) {

        if(h2rep.existsById(s.getRoll_id())&&postgreRep.existsById(s.getRoll_id())) {
    		throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Student Already Exists!");
    	}
    	
        h2rep.save(s);

//        if (true) {
//            throw new RuntimeException("Testing JTA rollback");
//        }

        postgreRep.save(s);
    }
}
