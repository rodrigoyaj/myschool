package com.myschool.service;

import com.myschool.exception.StudentNotFoundException;
import com.myschool.model.Student;
import com.myschool.repository.StudentRepository;
import com.myschool.representation.DiscountRO;
import com.myschool.representation.StudentRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
public class ScholarshipService {

    private RestTemplate restTemplate;
    private StudentRepository studentRepository;
    private Environment env;
    private Mono mono;
    private WebClient webClient= WebClient.create("http://localhost:8080/api/v1/myschool/students");

    //new code

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

        // restTemplate
        /*ResponseEntity<String> response = restTemplate.exchange(path,
                HttpMethod.POST, request, String.class);*/

        //webFlux
        String cad = webClient.post()
                .uri(path)
                .body(Mono.just(studentRO), ResponseEntity.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ResponseEntity<String> response=new ResponseEntity<>(cad,HttpStatus.OK);

        //restTemplate
        if(response.getStatusCode() != HttpStatus.OK)
            throw new Exception("Something went wrong");
        System.out.println(response.getBody());


    }

    public DiscountRO getStudentDiscount(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discount");

        ResponseEntity<DiscountRO> response = null;
        Mono<DiscountRO> response2 = null;
        try{
            // restTemplate
            //response = restTemplate.getForEntity(path, DiscountRO.class, studentId);

            //webFlux
            response2 = webClient.get()
                    .uri(path,studentId)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                        if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
                            /*return Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND,
                                    "Entity not found buuuuu."));*/
                            return Mono.empty();
                        } else {
                            //return Mono.error(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
                            return Mono.empty();
                        }
                    })
                    .bodyToMono(DiscountRO.class);

        }catch (final HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new StudentNotFoundException();
        }

        //restTemplate
       /*if (response.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception("Something went wrong.");
        }
        return response.getBody();*/


        //webFlux
        //System.out.println(response2.block().getStudentId());
        return response2.block();
    }

    public DiscountRO getStudentDiscountWithCSVFormat(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discountwithcsvformat");

        // TODO: Accept text/customcsv
        ResponseEntity<String> response =
                restTemplate.getForEntity(path, String.class, studentId);

        if(response.getStatusCode() != HttpStatus.OK)
        {
            System.out.println(response.getBody());
            System.out.println(response.getStatusCode());
            throw new Exception("Something went wrong");
        }

        System.out.println(response.getBody());

        return new DiscountRO();
    }


}
