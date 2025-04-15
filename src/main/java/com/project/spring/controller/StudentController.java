package com.project.spring.controller;

import com.project.spring.model.Major;
import com.project.spring.model.Student;

import com.project.spring.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

        private final StudentService studentService;

        // Path to save uploaded images
        private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

        // List all students with pagination and optional search keyword
        @GetMapping
        public String listStudents(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "") String keyword) {
            Page<Student> students = studentService.getAllStudents(keyword, PageRequest.of(page, 5));
            model.addAttribute("students", students);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "students/list";
        }

        // Show the student creation form
        @GetMapping("/create")
        public String showCreateForm(Model model) {
            model.addAttribute("student", new Student());
            model.addAttribute("majors", Major.values());
            model.addAttribute("years", new int[]{1, 2, 3, 4});
            return "students/create";
        }

        // Handle student creation
        @PostMapping("/create")
        public String createStudent(@ModelAttribute Student student,
                                    @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

            if (!imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                student.setImagePath("/uploads/" + fileName);
            }

            studentService.createStudent(student);
            return "redirect:/students";
        }

        // Show update form
        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model) {
            Student student = studentService.getStudentById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            model.addAttribute("student", student);
            model.addAttribute("majors", Major.values());
            model.addAttribute("years", new int[]{1, 2, 3, 4});
            return "students/edit";
        }

        // Handle update
        @PostMapping("/update/{id}")
        public String updateStudent(@PathVariable Long id,
                                    @ModelAttribute Student student,
                                    @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

            if (!imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                student.setImagePath("/uploads/" + fileName);
            }

            studentService.updateStudent(id, student);
            return "redirect:/students";
        }

        // Delete student
        @GetMapping("/delete/{id}")
        public String deleteStudent(@PathVariable Long id) {
            studentService.deleteStudent(id);
            return "redirect:/students";
        }

}
