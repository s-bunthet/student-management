# Student Management  Web Application

A simple web application for managing students, courses, and enrollments. This project is built using Spring Boot and follows the MVC architecture. 
It includes features for user authentication, role-based access control, and CRUD operations for students and courses.

## 🔧 Tech Stack Summary

| Component         | Choice                                |
|-------------------|---------------------------------------|
| Backend           | Spring Boot (with MVC, Security, and JPA) |
| Dependency Manager | Maven                                 |
| DB                | PostgreSQL                            |
| ORM               | Hibernate                             |
| Logging           | Log4j                                 |
| Security          | Spring Security                       |
| Testing           | JUnit                                 |
| Frontend          | Thymeleaf (or React for SPA)          |
| Webserver         | Tomcat (embedded or external)         |
| CI/CD             | Jenkins                               |



student-management/ <br/>
├── src/ <br/>
│   ├── main/ <br/>
│   │   ├── java/com/example/studentmanagement/ <br/>
│   │   │   ├── controller/ <br/>
│   │   │   ├── model/ <br/>
│   │   │   ├── repository/ <br/>
│   │   │   ├── service/ <br/>
│   │   │   ├── config/ <br/>
│   │   │   └── StudentManagementApplication.java <br/>
│   │   └── resources/ <br/>
│   │       ├── static/          # CSS, JS, images <br/>
│   │       ├── templates/       # HTML files (Thymeleaf) <br/>
│   │       └── application.properties <br/>
│   └── test/java/... <br/>
└── pom.xml <br/>



User → Controller → StudentService (interface) → StudentServiceImpl → StudentRepository → Database
