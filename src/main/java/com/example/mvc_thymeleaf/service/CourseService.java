package com.example.mvc_thymeleaf.service;
import com.example.mvc_thymeleaf.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAll();
    Course getById(Long id);
}
