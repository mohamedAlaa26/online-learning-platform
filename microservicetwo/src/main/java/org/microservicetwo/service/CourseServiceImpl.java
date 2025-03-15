package org.microservicetwo.service;

import org.microservicetwo.entities.Course;
import org.microservicetwo.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{


    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course presestCoursefrommicroservice2(Course course) {
        return courseRepository.save(course);
    }
}
