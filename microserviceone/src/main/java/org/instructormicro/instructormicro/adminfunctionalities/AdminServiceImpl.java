package org.instructormicro.instructormicro.adminfunctionalities;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.instructormicro.instructormicro.entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AdminServiceImpl implements AdminService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Users> getAllUsers() throws IOException {
        List<Users> allUsers = new ArrayList<>();

        // Retrieve all instructors
        List<Instructor> instructors = entityManager.createQuery("SELECT i FROM Instructor i", Instructor.class).getResultList();
        for (Instructor instructor : instructors) {
            Users user = new Users();
            user.setId(instructor.getId());
            user.setName(instructor.getName());
            user.setEmail(instructor.getEmail());
            user.setType("instructor");
            // Set other user attributes as needed
            allUsers.add(user);
        }

        // Retrieve all students from the API
        URL url = new URL("http://localhost:8082/api/students/GetAllStudents");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            String jsonResponse = response.toString();
            jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1); // Remove the surrounding square brackets
            String[] studentsArray = jsonResponse.split("},\\{"); // Split the response into individual student JSON strings
            for (int i = 0; i < studentsArray.length; i++) {
                if (i == 0) {
                    studentsArray[i] = studentsArray[i] + "}";
                } else if (i == studentsArray.length - 1) {
                    studentsArray[i] = "{" + studentsArray[i];
                } else {
                    studentsArray[i] = "{" + studentsArray[i] + "}";
                }

                // Assuming the format of each student JSON is well-structured
                String studentJson = studentsArray[i];
                studentJson = studentJson.replace("{", "").replace("}", "").replace("\"", ""); // Remove curly braces and quotes
                String[] keyValuePairs = studentJson.split(",");
                Users user = new Users();
                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "id":
                            user.setId(Long.parseLong(value));
                            break;
                        case "name":
                            user.setName(value);
                            break;
                        case "email":
                            user.setEmail(value);
                            break;
                        case "password":
                            // Handle password as needed
                            break;
                        case "affiliation":
                            // Handle affiliation as needed
                            break;
                        case "bio":
                            // Handle bio as needed
                            break;
                    }
                }
                user.setType("student");

                // Check if the user already exists
                if (isUserExist(user.getId())) {
                    entityManager.merge(user);
                } else {
                    user.setId(null); // Set ID to null to indicate a new entity
                    entityManager.persist(user);
                }
                allUsers.add(user);
            }
        }

        return allUsers;
    }

    private boolean isUserExist(Long userId) {
        return entityManager.createQuery("SELECT COUNT(u) FROM Users u WHERE u.id = :id", Long.class)
                .setParameter("id", userId)
                .getSingleResult() > 0;
    }

    @Override
    public Admin login(String username, String password) {
        // Check if the provided username and password match the static admin account
        if (username.equals("admin") && password.equals("admin")) {
            // Create and return the static admin object
            Admin admin = new Admin();
            admin.setName(username);
            admin.setPassword(password);
            return admin;
        }
        // If the provided credentials do not match, return null
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return entityManager.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }

    @Override
    public boolean updateCourseStatus(Long courseId, String status) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            course.setStatus(status);
            entityManager.merge(course);
            return true;
        }
        return false;
    }

    @Override
    public boolean editCourse(Long courseId, Course updatedCourse) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            // Update course details
            course.setName(updatedCourse.getName());
            course.setDuration(updatedCourse.getDuration());
            course.setCategory(updatedCourse.getCategory());
            course.setRating(updatedCourse.getRating());
            course.setCapacity(updatedCourse.getCapacity());
            course.setNumberOfEnrolledStudents(updatedCourse.getNumberOfEnrolledStudents());
            course.setStatus(updatedCourse.getStatus());

            entityManager.merge(course); // Persist the changes to the database
            return true;
        } else {
            return false; // Course not found
        }
    }

    @Override
    public boolean removeCourse(Long courseId) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            entityManager.remove(course); // Remove the course from the database
            return true;
        } else {
            return false; // Course not found
        }
    }

    @Override
    // Method to get the number of students
    public int getNumStudents() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM Users u WHERE u.Type = 'student'", Long.class);
        return Math.toIntExact(query.getSingleResult());
    }

    @Override
    // Method to get the number of instructors
    public int getNumInstructors() throws IOException {
        int count = 0;
        List<Users> users = getAllUsers();
        for (Users user : users) {
            if (user.getType().equals("instructor")) {
                count++;
            }
        }
        return count;
    }

    @Override
    // Method to get the number of courses
    public int getNumCourses() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Course c", Long.class);
        return Math.toIntExact(query.getSingleResult());
    }

    @Override
    // Method to get the number of accepted courses
    public int getNumAcceptedCourses() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Course c WHERE c.Status = 'Approved'", Long.class);
        return Math.toIntExact(query.getSingleResult());
    }

    @Override
    // Method to get the number of rejected courses
    public int getNumRejectedCourses() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Course c WHERE c.Status = 'Rejected'", Long.class);
        return Math.toIntExact(query.getSingleResult());
    }

    @Override
    public String getCourseStatus(Long courseId) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            return course.getStatus();
        }
        return null;
    }
}
