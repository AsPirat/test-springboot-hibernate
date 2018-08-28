package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Resource;
import com.example.mvc_thymeleaf.entity.Statistics;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    void calculateTermsWeight(Map<String, Double> sorted_key_words);
    void updateWeight(Map<String, Double> weight_words, Resource resource);
    List<SearchReport> calculateRelevance(List<String> key_words, List<Long> id_disc);
    Page<Statistics> getPage(Integer pageNumber);
    List<Statistics> getAll();
    void deleteAll();
}
