package org.instructormicro.instructormicro.adminfunctionalities;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.instructormicro.instructormicro.entities.Admin;
import org.instructormicro.instructormicro.entities.Course;
import org.instructormicro.instructormicro.entities.Users;

import java.io.IOException;
import java.util.List;

@Stateful
@Path("/admins")
public class AdminController {

    @Inject
    private AdminService adminService;

    // View user accounts
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewUserAccounts() throws IOException {
        // Retrieve user accounts
        List<Users> users = adminService.getAllUsers();

        // Return user accounts
        return Response.ok().entity(users).build();
    }

    // Admin login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginAdmin(@FormParam("username") String username, @FormParam("password") String password) {
        // Perform admin login
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            return Response.ok().entity(admin).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // View all courses
    @GET
    @Path("/viewAll/courses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllCourses() {
        List<Course> courses = adminService.getAllCourses();
        return Response.ok().entity(courses).build();
    }

    // Update course status
    @PUT
    @Path("/publishCheck/courses/{courseId}/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourseStatus(
            @PathParam("courseId") Long courseId,
            @PathParam("status") String status
    ) {
        // Validate input parameters
        if (courseId == null || status == null || status.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input parameters").build();
        }

        // Check if the status is same as the current status of the course
        String currentStatus = adminService.getCourseStatus(courseId);
        if (currentStatus.equals(status)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Course is already " + status).build();
        }

        // Update course status
        boolean updated = adminService.updateCourseStatus(courseId, status);
        if (updated) {
            return Response.ok().entity("Course status updated successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Course not found").build();
        }
    }

    @PUT
    @Path("/edit/courses/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCourse(
            @PathParam("courseId") Long courseId,
            Course updatedCourse
    ) {
        boolean edited = adminService.editCourse(courseId, updatedCourse);
        if (edited) {
            return Response.ok().entity("Course updated successfully").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Only admins can edit courses").build();
        }
    }

    @DELETE
    @Path("/remove/courses/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCourse(@PathParam("courseId") Long courseId) {
        boolean removed = adminService.removeCourse(courseId);
        if (removed) {
            return Response.ok().entity("Course removed successfully").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Only admins can remove courses").build();
        }
    }

    // Get platform usage metrics
    @GET
    @Path("/platform-usage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlatformUsageMetrics() throws IOException {
        // Retrieve metrics from the service
        int numStudents = adminService.getNumStudents();
        int numInstructors = adminService.getNumInstructors();
        int numCourses = adminService.getNumCourses();
        int numAcceptedCourses = adminService.getNumAcceptedCourses();
        int numRejectedCourses = adminService.getNumRejectedCourses();

        // Construct the response JSON object
        JsonObject responseJson = Json.createObjectBuilder()
                .add("numStudents", numStudents)
                .add("numInstructors", numInstructors)
                .add("numCourses", numCourses)
                .add("numApprovedCourses", numAcceptedCourses)
                .add("numRejectedCourses", numRejectedCourses)
                .build();

        // Return the response
        return Response.ok().entity(responseJson).build();
    }

}
