package org.microservicetwo.service;


import org.microservicetwo.entities.Course;
import org.microservicetwo.entities.Enrollment;
import org.microservicetwo.entities.Student;
import org.microservicetwo.repositories.EnrollmentRepository;
import org.microservicetwo.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> loginStudent(String email, String password) {
        Optional<Student> student = studentRepository.findByEmail(email);
        return student.filter(s -> s.getPassword().equals(password));
    }

    @Override
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId).
                orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Enrollment> getCurrentAndPastEnrollments(Long studentId) {
        
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Course> getAllCourses() {
        String url = "http://localhost:8080/microserviceone-1.0-SNAPSHOT/api/admins/viewAll/courses";
        List<Course> courses = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {
                }
        ).getBody();
        return courses;
    }

    @Override
    public Course getCourseDetails(Long courseId) {
        String url = "http://localhost:8080/microserviceone-1.0-SNAPSHOT/api/admins/viewAll/courses";
        List<Course> courses = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {
                }
        ).getBody();

        return courses.stream().filter(course -> course.getId().equals(courseId)).findFirst().orElse(null);
    }

    @Override
    public List<Course> searchCourses(String name, String category, boolean sortByRating) {
        String url = "http://localhost:8080/microserviceone-1.0-SNAPSHOT/api/admins/viewAll/courses";
        List<Course> courses = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Course>>() {
                }
        ).getBody();
        // Implement filtering and sorting logic here
        return courses;
    }

    @Override
    public boolean makeEnrollment(Long studentId, Long courseId) {
        // Check if the enrollment already exists
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            // Enrollment already exists, return false
            return false;
        }

        // Get course details
        Course course = getCourseDetails(courseId);
        if (course == null) {
            // Course not found, enrollment cannot be made
            return false;
        }

        // Create a new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(studentRepository.findById(studentId).orElse(null));
        enrollment.setCourse(course);
        enrollment.setEnrollmentStatus("Pending"); // Set enrollment status to current

        // Save the enrollment
        enrollmentRepository.save(enrollment);


        return true;
    }

    public boolean cancelEnrollment(Long studentId, Long courseId) {
        // Check if the enrollment exists
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            // Enrollment does not exist, return false
            return false;
        }

        // Get the enrollment
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (enrollment == null) {
            // Enrollment not found, return false
            return false;
        }

        // Delete the enrollment
        enrollmentRepository.delete(enrollment);

        return true;
    }

    @Override
    public void notifyCourseEnrollmentUpdate(Long studentId, Long courseId) {
        // Implement notification logic
    }

    @Override
    public boolean addCourseReview(Long studentId, Long courseId, String review, double rating) {
        // Implement review logic
        return true;
    }


}
