package com.myschool.repository;

import com.myschool.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("Select s from Student s where s.email like %?1%")
    public List<Student> getStudentByEmail(String email);
}