package com.myschool.resource;

import com.myschool.model.Student;
import com.myschool.representation.DiscountRO;
import com.myschool.service.ScholarshipService;
import com.myschool.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/myschool/students")
public class StudentResource {
    private StudentService studentService;
    private ScholarshipService scholarshipService;

    @Autowired
    public StudentResource(StudentService studentService, ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents(@RequestParam(required = false) String email)
    {
        if(email != null)
            return studentService.getStudentByEmail(email);
        else
            return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable long id)
    {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public Student registerStudent(@RequestBody Student student)
    {
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id)
    {
        studentService.deleteStudent(id);
    }

    @PostMapping("/{id}/sendtoscholarshipsystem")
    public void sendToScholarShipSystem(@PathVariable long id) throws Exception {
        scholarshipService.sendStudent(id);
    }

    @GetMapping("/{id}/discount")
    public ResponseEntity<DiscountRO>
        getStudentDiscount(@PathVariable long id) throws Exception {
        DiscountRO discountRO = scholarshipService.getStudentDiscount(id);
        return new ResponseEntity<>(discountRO, HttpStatus.OK);
    }

    @GetMapping("/{id}/discountfromcsvformat")
    public ResponseEntity<DiscountRO> getStudentDiscountFromCSVFormat(@PathVariable long id) throws Exception {
        DiscountRO discountRO = scholarshipService.getStudentDiscountWithCSVFormat(id);
        return new ResponseEntity<>(discountRO, HttpStatus.OK);
    }
}
