package com.myschool.resource;

import com.myschool.model.Student;
import com.myschool.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("api/v1/myschool/students")
public class StudentResource {
    private StudentService studentService;

    public StudentResource(StudentService studentService) {
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
        return studentService.registerStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id)
    {
        studentService.deleteStudent(id);
    }
}
