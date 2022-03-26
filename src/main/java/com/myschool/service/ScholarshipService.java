package com.myschool.service;

import com.myschool.exception.StudentNotFoundException;
import com.myschool.model.Student;
import com.myschool.repository.StudentRepository;
import com.myschool.representation.DiscountRO;
import com.myschool.representation.StudentRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ScholarshipService {

    private RestTemplate restTemplate;
    private StudentRepository studentRepository;
    private Environment env;

    @Autowired
    public ScholarshipService(Environment env,
                              RestTemplate restTemplate,
                              StudentRepository studentRepository)
    {
        this.env = env;
        this.restTemplate = restTemplate;
        this.studentRepository = studentRepository;
    }

    public void sendStudent(long studentId) throws Exception {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(studentOptional.orElse(null) == null)
            return;

        Student student = studentOptional.get();
        StudentRO studentRO = new StudentRO(student);

        String path = env.getProperty("scholarship.paths.register");

        HttpEntity<StudentRO> request = new HttpEntity<>(studentRO);

        // TODO: Use WebFlux
        ResponseEntity<String> response = restTemplate.exchange(path,
                HttpMethod.POST, request, String.class);

        if(response.getStatusCode() != HttpStatus.OK)
            throw new Exception("Something went wrong");

        System.out.println(response.getBody());
    }

    public DiscountRO getStudentDiscount(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discount");

        ResponseEntity<DiscountRO> response = null;
        try{
            // TODO: Use WebFlux
            response = restTemplate.getForEntity(path, DiscountRO.class, studentId);
        }catch (final HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new StudentNotFoundException();
        }

        if (response.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception("Something went wrong.");
        }
        return response.getBody();
    }

    public DiscountRO getStudentDiscountWithCSVFormat(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discountwithcsvformat");

        // TODO: Accept text/customcsv
        ResponseEntity<DiscountRO> response =
                restTemplate.getForEntity(path, DiscountRO.class, studentId);

        if(response.getStatusCode() != HttpStatus.OK)
        {
            System.out.println(response.getBody());
            System.out.println(response.getStatusCode());
            throw new Exception("Something went wrong");
        }

        return response.getBody();
    }
}
