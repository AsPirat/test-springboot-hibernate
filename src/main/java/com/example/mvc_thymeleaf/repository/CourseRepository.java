package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
