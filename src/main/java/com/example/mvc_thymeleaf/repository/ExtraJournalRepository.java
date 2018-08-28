package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.ExtraJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraJournalRepository extends JpaRepository<ExtraJournal, Long> {
    @Query("from ExtraJournal ex where ex.id>=:id_oper")
    List<ExtraJournal> getNewRecords(@Param("id_oper") Long id_oper);
}
