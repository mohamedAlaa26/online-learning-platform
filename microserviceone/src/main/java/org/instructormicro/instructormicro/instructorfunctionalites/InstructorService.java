package org.instructormicro.instructormicro.instructorfunctionalites;

import org.instructormicro.instructormicro.entities.Course;
import org.instructormicro.instructormicro.entities.Instructor;

import java.io.IOException;
import java.net.MalformedURLException;

public interface InstructorService {
    void save(Instructor instructor);

    boolean isInstructorRegistered(String email);

    boolean isInstructorLoggedIn(String name);

    void createCourse(Course course) throws IOException;

    Instructor getInstructorIdByName(String instructorName);
}

