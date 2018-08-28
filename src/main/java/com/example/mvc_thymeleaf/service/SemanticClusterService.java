package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.SemanticCluster;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SemanticClusterService {

    List<Long> findIdUniqueWords();
    List<ClusterReport> findWeightWords(Long id_word);
    void updateSemanticCluster();
    Double calculateWeightLength(Collection<Double> weight_vector);
    Map<Long, Double> sortWordWeight(Map<Long, Double> word_vector);
    List<String> extendQueryWord(String key_term);
    Page<SemanticCluster> getPage(Integer pageNumber);
    List<SemanticCluster> getAll();
    void deleteAll();
    List<SemanticCluster> sortCluster();
}
