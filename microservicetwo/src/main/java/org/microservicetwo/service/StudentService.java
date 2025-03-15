package org.microservicetwo.service;

import org.microservicetwo.entities.Course;
import org.microservicetwo.entities.Enrollment;
import org.microservicetwo.entities.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student registerStudent(Student student);
    Optional<Student> loginStudent(String email, String password);
    Student getStudentById(Long studentId);
    List<Student> getAllStudents();
    public List<Enrollment> getCurrentAndPastEnrollments(Long studentId);
    public List<Course> getAllCourses();
    public Course getCourseDetails(Long courseId);
    public List<Course> searchCourses(String name, String category, boolean sortByRating);
    public boolean makeEnrollment(Long studentId, Long courseId);
    public boolean cancelEnrollment(Long studentId, Long courseId);
    public void notifyCourseEnrollmentUpdate(Long studentId, Long courseId);
    public boolean addCourseReview(Long studentId, Long courseId, String review, double rating);


}
