package org.microservicetwo.controllers;

import org.microservicetwo.entities.Course;
import org.microservicetwo.entities.Enrollment;
import org.microservicetwo.entities.Student;
import org.microservicetwo.service.CourseService;
import org.microservicetwo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {

        return ResponseEntity.ok(studentService.registerStudent(student));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginStudent(@RequestParam String email, @RequestParam String password) {
        if (studentService.loginStudent(email, password).isPresent()) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @GetMapping("/GetStudentById")
    public ResponseEntity<Student> getStudent(@RequestParam Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping("/GetAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{studentId}/enrollments")
    public ResponseEntity<List<Enrollment>> viewEnrollments(@PathVariable Long studentId) {
        List<Enrollment> enrollments = studentService.getCurrentAndPastEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/allCourses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(studentService.getAllCourses());
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable Long courseId) {
        Course course = studentService.getCourseDetails(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String category,
                                                      @RequestParam(required = false, defaultValue = "false") boolean sortByRating) {
        List<Course> courses = studentService.searchCourses(name, category, sortByRating);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courseEnroll")
    public ResponseEntity<String> makeEnrollment(@RequestParam("studentId") Long studentId, @RequestParam("courseId") Long courseId) {
        if (studentService.makeEnrollment(studentId, courseId)) {
            return ResponseEntity.ok("Enrollment successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enrollment failed");
        }
    }

    @DeleteMapping("/course-cancel-enrollment")
    public ResponseEntity<String> cancelEnrollment(@RequestParam("studentId") Long studentId, @RequestParam("courseId") Long courseId) {
        if (studentService.cancelEnrollment(studentId, courseId)) {
            return ResponseEntity.ok("Enrollment cancelled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enrollment cancellation failed");
        }
    }
}
