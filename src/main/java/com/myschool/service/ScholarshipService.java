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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ScholarshipService {

  //  private RestTemplate restTemplate;
    private StudentRepository studentRepository;
    private Environment env;
    private final WebClient defaultWebClient;

    @Autowired
    public ScholarshipService(Environment env,
                           //   RestTemplate restTemplate,
                              StudentRepository studentRepository,  WebClient defaultWebClient )
    {
        //
        this.env = env;
      // this.restTemplate = restTemplate;
        this.studentRepository = studentRepository;
        this.defaultWebClient = defaultWebClient;
    }

    public void sendStudent(long studentId) throws Exception {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
       // System.out.println(studentOptional);
        if(studentOptional.orElse(null) == null)
            return;

        Student student = studentOptional.get();
        StudentRO studentRO = new StudentRO(student);

        String path = env.getProperty("scholarship.paths.register");
       // System.out.println(student);
       // System.out.println(studentRO);
        // TODO: Use WebFlux
        this.defaultWebClient
                 .post()
                 .uri("/student")
                 .body(BodyInserters.fromValue(studentRO))
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
    }

    public DiscountRO getStudentDiscount(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discount");
        // TODO: Use WebFlux
        DiscountRO result;
        result= this.defaultWebClient
                .get()
                .uri("/student/"+studentId+"/discount")
                .retrieve()
               .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(), response -> {
                   System.out.println("NOT FOUND error");
                   return Mono.error(new StudentNotFoundException());
                })
                .bodyToMono(DiscountRO.class)
                .block();
                return result;
    }

    public DiscountRO getStudentDiscountWithCSVFormat(long studentId) throws Exception {
        String path = env.getProperty("scholarship.paths.discountwithcsvformat");
        DiscountRO discount = new DiscountRO();
        // TODO: Use WebFlux
        String result;
            result = this.defaultWebClient
                    .get()
                    .uri("/student/" + studentId + "/discountwithcsvformat")
                    //.header("Content-type","text/csv")
                    .accept(new MediaType("text", "customcsv"))
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value(), response -> {
                        System.out.println("Internal error, posible NOT FOUND");
                        return Mono.error(new StudentNotFoundException());
                    })
                    .bodyToMono(String.class)
                    .block();
          String []values=  result.split(",");
        discount.setStudentId(Long.parseLong(values[0]));
        discount.setDisc(Integer.parseInt(values[1]));
        return discount;
    }
    public Student  updateStudent(Student student) throws Exception {
      //  System.out.println("Service "+student1.getId());
          return this.defaultWebClient
                .put()
                .uri("/student/update")
                .body(Mono.just(student), Student.class)
                .retrieve()
                .bodyToMono(Student.class)
                .block();
    }
}
