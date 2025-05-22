package com.project.spring.controller;

import com.project.spring.model.Major;
import com.project.spring.model.Student;

import com.project.spring.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

        private final StudentService studentService;

        // Path to save uploaded images
        private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

        // List all students with pagination and optional search keyword
        @GetMapping
        public String listStudents(
                @RequestParam(defaultValue = "") String keyword,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "firstName") String sortField,
                @RequestParam(defaultValue = "asc") String sortDir,
                Model model) {
            log.info("Listing students with keyword: {}, page: {}, size: {}, sortField: {}, sortDir: {}", keyword, page, size, sortField, sortDir);
            Sort sort = sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Student> studentPage = studentService.getAllStudents(keyword, pageable);

            model.addAttribute("students", studentPage);
            model.addAttribute("keyword", keyword);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "students/list";
        }

        // Show the student creation form
        @GetMapping("/create")
        public String showCreateForm(Model model) {
            log.info("Creating a new student");
            model.addAttribute("student", new Student());
            model.addAttribute("majors", Major.values());
            model.addAttribute("years", new int[]{1, 2, 3, 4});
            return "students/create";
        }

        // Handle student creation
        @PostMapping("/create")
        public String createStudent(@ModelAttribute Student student,
                                    @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

            log.info("Creating student: {}", student);
            if (!imageFile.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
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
            log.info("Editing student with ID: {}", id);
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

            log.info("Updating student with ID: {}", id);
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
            log.info("Deleting student with ID: {}", id);
            studentService.deleteStudent(id);
            return "redirect:/students";
        }

}
