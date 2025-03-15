package org.instructormicro.instructormicro.adminfunctionalities;

import org.instructormicro.instructormicro.entities.Admin;
import org.instructormicro.instructormicro.entities.Course;
import org.instructormicro.instructormicro.entities.Student;
import org.instructormicro.instructormicro.entities.Users;

import java.io.IOException;
import java.util.List;

public interface AdminService {
//    public List<Student> getAllStudents() throws IOException;
    public List<Users> getAllUsers() throws IOException;

    Admin login(String username, String password);

    public List<Course> getAllCourses();

    public boolean updateCourseStatus(Long courseId, String status);

    public boolean editCourse(Long courseId, Course updatedCourse);

    public boolean removeCourse(Long courseId);

    public int getNumStudents();

    public int getNumInstructors() throws IOException;

    public int getNumCourses();

    public int getNumAcceptedCourses();

    public int getNumRejectedCourses();

    public String getCourseStatus(Long courseId);
}
