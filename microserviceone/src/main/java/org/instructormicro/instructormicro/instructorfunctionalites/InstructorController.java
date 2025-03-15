package org.instructormicro.instructormicro.instructorfunctionalites;

import jakarta.ejb.*;
import org.instructormicro.instructormicro.entities.Course;
import org.instructormicro.instructormicro.entities.Instructor;
import org.instructormicro.instructormicro.util.utilities;

import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.IOException;
import java.net.MalformedURLException;

@Stateful
@Path("/instructors")
public class InstructorController {

    @Inject
    private InstructorService instructorService;

    // Register an instructor
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerInstructor(Instructor instructor) {
        // Validate instructor information (e.g., check for required fields, validate email format, etc.)
        if (instructor == null || instructor.getEmail() == null || instructor.getEmail().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid instructor data").build();
        }
        // Validate email format
        if (!utilities.isValidEmail(instructor.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid email format").build();
        }
        // Validate name
        if (instructor.getName() == null || instructor.getName().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name is required").build();
        }
        // Validate password and should be more than 8 characters
        if (instructor.getPassword() == null || instructor.getPassword().length() < 8) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is required and should be at least 8 characters").build();
        }
        // Validate affiliation
        if (instructor.getAffiliation() == null || instructor.getAffiliation().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Affiliation is required").build();
        }
        // Validate years of experience
        if (instructor.getYearsOfExperience() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Years of experience should be greater than 0").build();
        }
        // Validate bio
        if (instructor.getBio() == null || instructor.getBio().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bio is required").build();
        }

        // Save instructor to the database (you'll need to inject a service or repository for this)
        instructorService.save(instructor);

        // Return success response
        return Response.status(Response.Status.CREATED).entity("Instructor registered successfully").build();
    }

    // Check if an instructor is registered
    @GET
    @Path("/check-registration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkInstructorRegistration(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        boolean isRegistered = instructorService.isInstructorRegistered(email);
        if (isRegistered) {
            return Response.ok().entity("Instructor with email " + email + " is registered").build();
        } else {
            return Response.ok().entity("Instructor with email " + email + " is not registered").build();
        }
    }

    // Login an instructor
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginInstructor(Instructor instructor) {
        // Validate instructor information (e.g., check for required fields, validate email format, etc.)
        if (instructor == null || instructor.getEmail() == null || instructor.getEmail().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid instructor data").build();
        }
        // Validate email format
        if (!utilities.isValidEmail(instructor.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid email format").build();
        }
        // Validate password
        if (instructor.getPassword() == null || instructor.getPassword().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Password is required").build();
        }

        // Check if the instructor is registered
        boolean isRegistered = instructorService.isInstructorRegistered(instructor.getEmail());
        if (!isRegistered) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Instructor with email " + instructor.getEmail() + " is not registered").build();
        }

        // Perform login logic (e.g., validate credentials, generate JWT token, etc.)
        // For simplicity, let's assume the instructor is logged in successfully
        return Response.ok().entity("Instructor logged in successfully").build();
    }

    // Create a course
    @POST
    @Path("/courses/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(Course course, @QueryParam("instructorName") String instructorName) throws IOException {
        // Check if the course is null
        if (course == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid course data").build();
        }

        // Get the instructor ID by name
        Instructor instructor = instructorService.getInstructorIdByName(instructorName);
        if (instructor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Instructor with name " + instructorName + " not found").build();
        }

        // Check if the instructor is registered or logged in
        boolean isLoggedIn = instructorService.isInstructorLoggedIn(instructorName);
        boolean isRegistered = instructorService.isInstructorRegistered(instructor.getEmail());
        if (!isRegistered) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Instructor with email " + instructor.getEmail() + " is not registered").build();
        }

        // Set the instructor ID for the course
        course.setInstructorid(instructor.getId());

        // Create the course
        instructorService.createCourse(course);

        // Return success response
        return Response.status(Response.Status.CREATED).entity("Course created successfully").build();
    }


}
