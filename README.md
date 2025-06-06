# 🎓 Learning Platform Microservices Project

A modular and scalable **online learning platform** that follows the microservices architecture paradigm. It handles student registration, course management, and enrollment workflows across two loosely coupled services using RESTful APIs.

This project demonstrates the combination of **AI/ML engineering skills** and **robust software architecture principles**, showcasing cloud readiness, microservices communication, and modular deployment.

---

## 🧩 Architecture Overview

The system consists of two primary microservices:

### 1. `microserviceone`
- **Tech Stack**: Jakarta EE, EJBs, H2 (in-memory DB), WildFly Application Server.
- **Responsibilities**: Core student data management, authentication, and instructor services.
- **Status**: Stateless REST APIs with EJB business logic.

### 2. `microservicetwo`
- **Tech Stack**: Spring Boot, MySQL, REST APIs.
- **Responsibilities**: Course management, student enrollment, admin endpoints.
- **Status**: Container-ready (e.g., Dockerized and deployable to cloud).

---

## 🗃️ Database Schema (`microservicetwo`)

```sql
CREATE TABLE Course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    category VARCHAR(255) NOT NULL,
    rating DOUBLE NOT NULL,
    capacity INT NOT NULL,
    numberOfEnrolledStudents INT NOT NULL,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE Student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    affiliation VARCHAR(100),
    bio TEXT
);

CREATE TABLE Enrollment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_status VARCHAR(50),
    FOREIGN KEY (student_id) REFERENCES Student(id),
    INDEX fk_student_idx (student_id),
    INDEX fk_course_idx (course_id)
);
