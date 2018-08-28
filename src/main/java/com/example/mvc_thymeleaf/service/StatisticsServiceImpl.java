package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Dictionary;
import com.example.mvc_thymeleaf.entity.Resource;
import com.example.mvc_thymeleaf.entity.Statistics;
import com.example.mvc_thymeleaf.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements  StatisticsService{
    private static final int PAGE_SIZE = 30;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public Page<Statistics> getPage(Integer pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, PAGE_SIZE);
        return statisticsRepository.findAll(request);
    }

    @Override
    public List<Statistics> getAll() {
        return statisticsRepository.findAll();
    }

    @Override
    public void deleteAll() {
        statisticsRepository.deleteAll();
    }

    @Override
    public void calculateTermsWeight(Map<String, Double> sorted_key_words) {
        for (Map.Entry<String, Double> entry : sorted_key_words.entrySet()) {
            Double TF = entry.getValue();
            String term = entry.getKey();
            Dictionary dictionary = dictionaryService.getByWord(term);
            Double weight = TF * dictionary.getIdf();
            sorted_key_words.put(term, weight);
        }
    }

    @Override
    public void updateWeight(Map<String, Double> weight_words, Resource resource) {

            for (Map.Entry<String, Double> entry : weight_words.entrySet()) {
                Statistics new_stat = new Statistics();
                new_stat.setWeight(entry.getValue());
                new_stat.setResource(resource);
                new_stat.setStat_dict(dictionaryService.getByWord(entry.getKey()));
                statisticsRepository.save(new_stat);
            }



    }

    @Override
    public List<SearchReport> calculateRelevance(List<String> key_words, List<Long> id_disc) {
        return statisticsRepository.calculateRelevance(key_words, id_disc);
    }


}
