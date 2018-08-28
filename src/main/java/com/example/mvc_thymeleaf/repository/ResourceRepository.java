package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("from Resource r where r.status = 'unprocessed'")
    List<Resource> findNewResources();

    @Query("select count(r.id_resource) from Resource r where r.discipline.id_discipline=:id_disc")
    Long countResourcesByDiscipline(@Param("id_disc") Long id_disc);

    @Query("select count(r.id_resource) from Resource r")
    Long countResources();

    @Query("select r.path from Resource r")
    List<String> getAllPathes();

    @Query("select r.path from Resource r where r.status = 'unprocessed'")
    List<String> getNewPathes();

    @Modifying
    @Query("update Resource r set r.status = 'processed' where r.status = 'unprocessed'")
    Integer updateStatus();
}