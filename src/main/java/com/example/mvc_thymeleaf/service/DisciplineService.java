package com.example.mvc_thymeleaf.service;
import com.example.mvc_thymeleaf.entity.Discipline;
import java.util.List;

public interface DisciplineService {
    List<Discipline> getAll();
    Discipline getById(Long id);
}