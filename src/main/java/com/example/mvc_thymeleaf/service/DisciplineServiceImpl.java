package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Discipline;
import com.example.mvc_thymeleaf.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;

    @Override
    public List<Discipline> getAll() {
        return disciplineRepository.findAll();
    }

    @Override
    public Discipline getById(Long id) {
        return disciplineRepository.getOne(id);
    }
}