package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.SemanticCluster;
import com.example.mvc_thymeleaf.repository.SemanticClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemanticClusterServiceImpl implements SemanticClusterService{
    private static final int PAGE_SIZE = 30;
    @Autowired
    private SemanticClusterRepository semanticClusterRepository;
    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<Long> findIdUniqueWords() {
        return semanticClusterRepository.findIdUniqueWords();
    }

    @Override
    public List<ClusterReport> findWeightWords(Long id_word) {
        return semanticClusterRepository.findWeightWords(id_word);
    }

    @Override
    public void updateSemanticCluster() {
        List<Long> unique_id_words = findIdUniqueWords();
        //int flush_counter = 0;
        for(Long id1: unique_id_words){
            List<ClusterReport> weight_word1 = findWeightWords(id1);
            if(weight_word1.size() > 1) {
                Map<Long, Double> map_word1 = convertToMap(weight_word1);
                Double length1 = calculateWeightLength(map_word1.values());
                Map<Long, Double> associated_vector = new HashMap<>();
                for (Long id2 : unique_id_words) {
                    Double proximity = 0.0;
                    if (!id1.equals(id2)) {
                        //change
                        List<ClusterReport> weight_word2 = findWeightWords(id2);
                        Map<Long, Double> map_word2 = convertToMap(weight_word2);
                        Double length2 = calculateWeightLength(map_word2.values());

                        for (Map.Entry<Long, Double> e1 : map_word1.entrySet()) {
                            for (Map.Entry<Long, Double> e2 : map_word2.entrySet()) {
                                if (e2.getKey().equals(e1.getKey())) {
                                    proximity += e1.getValue() * e2.getValue();
                                    weight_word2.remove(e2.getKey());
                                    break;
                                }
                            }
                        }
                        proximity /= length1 * length2;
                    }
                    if (!proximity.equals(1.0)) {
                        associated_vector.put(id2, proximity);
                    }
                }
                associated_vector = sortWordWeight(associated_vector);
                //List<SemanticCluster> new_cluster = new ArrayList<>();
                int limit = 5, i = 0;
                for (Map.Entry<Long, Double> sorted_entry : associated_vector.entrySet()) {
                    if (i < limit) {
                        SemanticCluster semanticCluster = new SemanticCluster();
                        semanticCluster.setFirst_dict(dictionaryService.getById(id1));
                        //semanticCluster.setSecond_dict(dictionaryService.getById(sorted_entry.getKey()));
                        semanticCluster.setSecond_dict(dictionaryService.getById(sorted_entry.getKey()));
                        semanticCluster.setProximity(sorted_entry.getValue());
                        semanticClusterRepository.save(semanticCluster);
                        //new_cluster.add(semanticCluster);
                        i++;
                    } else

                        break;
                }
                associated_vector.clear();
            }
            //semanticClusterRepository.saveAll(new_cluster);
            /*flush_counter++;
            if (flush_counter % 50 == 0) {
                semanticClusterRepository.flush();
            }*/
        }
        //semanticClusterRepository.flush();

    }
    private Map<Long,Double> convertToMap(List<ClusterReport> weight_word){
        Map<Long, Double> map_word = new HashMap<>();
        for (ClusterReport cr : weight_word) {
            map_word.put(cr.getId_resource(), cr.getWeight());
        }
        return map_word;
    }
    @Override
    public Double calculateWeightLength(Collection<Double> weight_vector) {
        Double length = 0.0;
        for(Double word_weght: weight_vector){
            length += word_weght * word_weght;
        }
        return Math.sqrt(length);
    }

    @Override
    public Map<Long, Double> sortWordWeight(Map<Long, Double> word_vector) {
        Map<Long, Double> sorted_word_vector = word_vector.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x1, x2) -> x1, LinkedHashMap::new));

        return sorted_word_vector;
    }

    @Override
    public List<String> extendQueryWord(String key_term) {
        return semanticClusterRepository.extendQueryWord(key_term);
    }

    @Override
    public Page<SemanticCluster> getPage(Integer pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, PAGE_SIZE);
        return semanticClusterRepository.findAll(request);
    }

    @Override
    public List<SemanticCluster> getAll() {
        return semanticClusterRepository.findAll();
    }

    @Override
    public void deleteAll() {
        semanticClusterRepository.deleteAll();
    }

    @Override
    public List<SemanticCluster> sortCluster() {
        return semanticClusterRepository.sortCluster();
    }
}
