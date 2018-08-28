package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Dictionary;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface DictionaryService {

    List<String> findDictWords();
    Long countWords();
    Dictionary getById(Long id);
    Dictionary getByWord(String word);
    List<Dictionary> getAll();
    void calculateDocsCountTerms(Map<String, Double> sorted_key_words);
    void updateIDF();
    Long getIdByWord(String word);
    Page<Dictionary> getPage(Integer pageNumber);

}
