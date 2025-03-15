package org.instructormicro.instructormicro.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String affiliation;
    private int yearsOfExperience;
    private String bio;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getBio() {
        return bio;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // toString method
    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", bio='" + bio + '\'' +
                '}';
    }
}
