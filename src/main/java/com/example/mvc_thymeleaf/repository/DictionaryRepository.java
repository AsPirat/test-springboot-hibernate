package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    @Query("select d.key_word from Dictionary d")
    List<String> findDictWords();

    @Query("select count(d.id) from Dictionary d")
    Long countWords();

    @Query("from Dictionary d where d.key_word=:word")
    Dictionary getByWord(@Param("word") String word);

    @Query("select d.id from Dictionary d where d.key_word=:word")
    Long getIdByWord(@Param("word") String word);

}
