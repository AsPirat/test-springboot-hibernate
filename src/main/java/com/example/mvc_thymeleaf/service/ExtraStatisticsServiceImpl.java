package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.ExtraStatistics;
import com.example.mvc_thymeleaf.entity.Resource;
import com.example.mvc_thymeleaf.repository.ExtraStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExtraStatisticsServiceImpl implements ExtraStatisticsService {
    @Autowired
    private ExtraStatisticsRepository extraStatisticsRepository;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    @Transactional
    public void updateTrainingData(Map<String, Double> sorted_key_words, Resource resource) {

        Long numStatWords = extraStatisticsRepository.countRecords();
        List<String> dict_words = extraStatisticsRepository.findStatWords(resource.getDiscipline().getId_discipline());
        if (numStatWords == 0 || dict_words.isEmpty()){
            for (Map.Entry<String, Double> DOC_words : sorted_key_words.entrySet()){
                ExtraStatistics stat = new ExtraStatistics();
                stat.setCount(DOC_words.getValue());
                stat.setDisc(resource.getDiscipline());
                stat.setDict(dictionaryService.getByWord(DOC_words.getKey()));
                extraStatisticsRepository.save(stat);
            }
        }
        else{
            Map<String, Boolean> WordInfo = new HashMap<>();
            String doc_word;
            Boolean state;
            Double count;
            for(String dict_word : dict_words){
                for (Map.Entry<String, Double> DOC_words : sorted_key_words.entrySet()){
                    doc_word = DOC_words.getKey();
                    ExtraStatistics stat = extraStatisticsRepository.getByTermDiscipline(resource.getDiscipline().getId_discipline(), dict_word);
                    if(stat != null && dict_word.equals(doc_word)){
                        count = stat.getCount() + DOC_words.getValue();
                        stat.setCount(count);
                        extraStatisticsRepository.save(stat);
                        state = true;
                        WordInfo.put(doc_word, state);
                    }
                    else{
                        state = WordInfo.get(doc_word);
                        if (state == null) {
                            WordInfo.put(doc_word, false);
                        } else {
                            WordInfo.put(doc_word, state);
                        }
                    }
                }
            }
            for (Map.Entry<String, Boolean> new_words : WordInfo.entrySet()) {
                if (!new_words.getValue()) {
                    String key = new_words.getKey();
                    ExtraStatistics stat = new ExtraStatistics();
                    stat.setCount(sorted_key_words.get(key));
                    stat.setDisc(resource.getDiscipline());
                    stat.setDict(dictionaryService.getByWord(key));
                    extraStatisticsRepository.save(stat);
                }
            }

        }

    }

    @Override
    public Long countDisciplineWords(Long id_dis) {
        return extraStatisticsRepository.countDisciplineWords(id_dis);
    }

    @Override
    public Double countDisciplineWord(Long id_term, Long id_disc) {
        return extraStatisticsRepository.countDisciplineWord(id_term, id_disc);
    }

    @Override
    public List<ExtraStatistics> getAll() {
        return extraStatisticsRepository.findAll();
    }

    @Override
    public void deleteAll() {
        extraStatisticsRepository.deleteAll();
    }
}