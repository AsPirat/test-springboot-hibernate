package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.ExtraStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraStatisticsRepository extends JpaRepository<ExtraStatistics, Long> {

    @Query("select count(es.id) from ExtraStatistics es")
    Long countRecords();

    @Query("select sum(es.count) from ExtraStatistics es where es.disc.id_discipline=:id_disc")
    Long countDisciplineWords(@Param("id_disc") Long id_dis);

    @Query("select es.count from ExtraStatistics es where es.dict.id=:id_term and es.disc.id_discipline=:id_disc")
    Double countDisciplineWord(@Param("id_term") Long id_term, @Param("id_disc") Long id_disc);

    @Query("select es.dict.key_word from ExtraStatistics es where es.disc.id_discipline=:id_disc")
    List<String> findStatWords(@Param("id_disc") Long id_disc);

    @Query("from ExtraStatistics es where es.disc.id_discipline=:id_disc and es.dict.key_word=:key_term")
    ExtraStatistics getByTermDiscipline(@Param("id_disc") Long id_disc, @Param("key_term") String key_term);
}