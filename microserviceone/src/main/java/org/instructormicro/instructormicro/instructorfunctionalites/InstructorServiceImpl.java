package org.instructormicro.instructormicro.instructorfunctionalites;

import jakarta.persistence.*;
import org.instructormicro.instructormicro.entities.*;

import jakarta.ejb.Stateless;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Stateless // or @Singleton depending on your requirements
public class InstructorServiceImpl implements InstructorService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Instructor instructor) {
        entityManager.persist(instructor);
    }

    @Override
    public boolean isInstructorRegistered(String email) {
        return entityManager.createQuery("SELECT COUNT(i) FROM Instructor i WHERE i.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0;

    }

    @Override
    public void createCourse(Course course) throws IOException {
        entityManager.persist(course);

        // add course in the microservice two
        // Prepare the course data for the second microservice
        String courseData = "{"
                + "\"name\":\"" + course.getName() + "\","
                + "\"duration\":" + course.getDuration() + ","
                + "\"category\":\"" + course.getCategory() + "\","
                + "\"rating\":" + course.getRating() + ","
                + "\"capacity\":" + course.getCapacity() + ","
                + "\"numberOfEnrolledStudents\":" + course.getNumberOfEnrolledStudents() + ","
                + "\"status\":\"" + course.getStatus() + "\""
                + "}";

        // Send the course data to the second microservice
        URL url = new URL("http://localhost:8082/api/courses/addCourse");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = courseData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to add course to microservice two. Response code: " + responseCode);
        }

        connection.disconnect();
    }

    @Override
    public Instructor getInstructorIdByName(String instructorName) {
        TypedQuery<Instructor> query = entityManager.createQuery("SELECT i FROM Instructor i WHERE i.name = :name", Instructor.class)
                .setParameter("name", instructorName);
        query.setMaxResults(1); // Get only one result

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null; // Instructor not found
        }
    }

    @Override
    public boolean isInstructorLoggedIn(String name) {
        return entityManager.createQuery("SELECT COUNT(i) FROM Instructor i WHERE i.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult() > 0;
    }
}

