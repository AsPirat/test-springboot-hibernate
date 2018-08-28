package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.ExtraStatistics;
import com.example.mvc_thymeleaf.entity.Resource;

import java.util.List;
import java.util.Map;

public interface ExtraStatisticsService {
    void updateTrainingData(Map<String, Double> sorted_key_words, Resource resource);
    Long countDisciplineWords(Long id_dis);
    Double countDisciplineWord(Long id_term, Long id_disc);
    List<ExtraStatistics> getAll();
    void deleteAll();
}