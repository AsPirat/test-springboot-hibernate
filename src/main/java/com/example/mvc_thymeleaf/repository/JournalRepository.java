package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    @Query("from Journal j order by j.date asc")
    List<Journal> sortJournal();
}
