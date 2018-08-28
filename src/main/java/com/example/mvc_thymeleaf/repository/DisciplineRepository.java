package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

}