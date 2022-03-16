package com.myschool.service;

import com.myschool.model.Student;
import com.myschool.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents()
    {
        return studentRepository.findAll();
    }

    public Student getStudentById(long id)
    {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElse(null);
    }

    public List<Student> getStudentByEmail(String email)
    {
        return studentRepository.getStudentByEmail(email);
    }

    public Student registerStudent(Student student)
    {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id)
    {
        studentRepository.deleteById(id);
    }
}