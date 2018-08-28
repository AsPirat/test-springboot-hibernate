package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Resource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ResourceService {

    Resource addResource(Resource res);
    List<Resource> findNewResources();
    Long countResources();
    List<String> getAllPathes();
    Resource getById(Long id);
    List<String> getNewPathes();
    List<Resource> getAll();
    List<String> extractDocumentText(String path);
    Map<String, Double> calculateTerms(List<String> TextDocument);
    Map<String, Double> sortKeyWords(Map<String, Double> key_words);
    Map<String, Double> removeInsignificantTerms(Map<String, Double> sort_dictionary, int limit);
    void calculateTermsTF(Map<String, Double> sorted_key_words);
    Integer updateStatus();
    Page<Resource> getPage(Integer pageNumber);
    Long countResourcesByDiscipline(Long id_disc);
}