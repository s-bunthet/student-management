package com.project.spring.service;

import com.project.spring.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);
    Student updateStudent(Long id, Student student);
    Optional<Student> getStudentById(Long id);
    void deleteStudent(Long id);
    Page<Student> getAllStudents(String keyword, Pageable pageable);
}