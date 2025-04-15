//StudentRepository is part of the Data Access Layer. It interacts directly with the database using Spring Data JPA.
//It extends JpaRepository, which gives you built-in methods like:
//save(), findById(), deleteById(), findAll(), etc.

package com.project.spring.repository;

import com.project.spring.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
}
