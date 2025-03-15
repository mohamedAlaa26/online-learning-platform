package org.microservicetwo.repositories;


import org.microservicetwo.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId);
}
