package org.instructormicro.instructormicro.entities;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration; // Duration in hours or weeks, etc.
    private String category;
    private double rating;
    private int capacity;
    private int numberOfEnrolledStudents;
    private String Status;

    @Column(name = "instructor_id")
    private Long instructorId;

    // Define the relationship with Instructor (not required for storing just the ID)
    // @ManyToOne
    // @JoinColumn(name = "instructor_id")
    // private Instructor instructor;

    // List of reviews (you might need to define a Review entity)
    // One-to-many relationship with reviews
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumberOfEnrolledStudents() {
        return numberOfEnrolledStudents;
    }

    public Long getInstructorid() {
        return instructorId;
    }

    public String getStatus() {
        return Status;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setNumberOfEnrolledStudents(int numberOfEnrolledStudents) {
        this.numberOfEnrolledStudents = numberOfEnrolledStudents;
    }

    public void setInstructorid(Long instructorId) {
        this.instructorId = instructorId;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    // Add a review
    public void addReview(Review review) {
        reviews.add(review);
        review.setCourse(this);
    }

    // Remove a review
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setCourse(null);
    }

    // toString method
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", capacity=" + capacity +
                ", numberOfEnrolledStudents=" + numberOfEnrolledStudents +
                ", instructorId=" + instructorId +
                ", Status='" + Status + '\'' +
                '}';
    }
}
