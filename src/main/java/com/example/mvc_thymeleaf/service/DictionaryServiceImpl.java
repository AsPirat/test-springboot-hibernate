package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Dictionary;
import com.example.mvc_thymeleaf.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    /*@Autowired
    private EntityManagerFactory entityManagerFactory;*/
    private static final int PAGE_SIZE = 40;
    @Autowired
    private DictionaryRepository dictRepository;
    @Autowired
    private ResourceService resourceService;

    @Override
    public Page<Dictionary> getPage(Integer pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, PAGE_SIZE);
        return dictRepository.findAll(request);
    }

    @Override
    public List<String> findDictWords() {
        return dictRepository.findDictWords();
    }

    @Override
    public Long countWords() {
        return dictRepository.countWords();
    }

    @Override
    public Dictionary getById(Long id) {
        return dictRepository.getOne(id);
    }

    @Override
    public Dictionary getByWord(String word) {
        return dictRepository.getByWord(word);
    }

    @Override
    public List<Dictionary> getAll() {
        return dictRepository.findAll();
    }

    @Override
    @Transactional
    public void calculateDocsCountTerms(Map<String, Double> sorted_key_words) {
        //Session session = entityManagerFactory.unwrap(SessionFactory.class).getCurrentSession();
        //Transaction tx = session.beginTransaction();
        Long flush_counter = 0L;
        Long numDictWords = countWords();
        if (numDictWords == 0){
            List<Dictionary> dictiki = new ArrayList<>();
            for (Map.Entry<String, Double> DOC_words : sorted_key_words.entrySet()){
                Dictionary dictionary = new Dictionary(1L, 0.0, DOC_words.getKey());
                //dictRepository.saveAndFlush(dictionary);
                dictRepository.save(dictionary);
                /*session.save(dictionary);
                flush_counter++;
                if (flush_counter % 5 == 0){
                    session.flush();
                    session.clear();
                }
                session.evict(dictionary);
                dictiki.add(dictionary);*/

            }
            //dictRepository.saveAll(dictiki);

        }
        else{
            //List<Dictionary> dictiki = new ArrayList<>();
            List<String> dict_words = findDictWords();
            Map<String, Boolean> WordInfo = new HashMap<>();
            String doc_word;
            Boolean state;
            Long count;
            for(String dict_word : dict_words){
                for (Map.Entry<String, Double> DOC_words : sorted_key_words.entrySet()){
                    doc_word = DOC_words.getKey();
                    if(dict_word.equals(doc_word)){
                        Dictionary old_dictionary = getByWord(dict_word);
                        count = old_dictionary.getResources_count();
                        old_dictionary.setResources_count(++count);
                        dictRepository.save(old_dictionary);
                        //dictiki.add(old_dictionary);
                        /*session.saveOrUpdate(old_dictionary);
                        flush_counter++;
                        if (flush_counter % 5 == 0){
                            session.flush();
                            session.clear();
                        }*/
                        //session.evict(old_dictionary);
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
                    Dictionary new_dictionary = new Dictionary(1L, 0.0, new_words.getKey());
                    dictRepository.save(new_dictionary);
                    //dictiki.add(new_dictionary);
                    /*session.saveOrUpdate(new_dictionary);
                    flush_counter++;
                    if (flush_counter % 5 == 0){
                        session.flush();
                        session.clear();
                    }*/
                    //session.evict(new_dictionary);
                }
            }

            //dictRepository.saveAll(dictiki);
            //session.close();
        }
        //tx.commit();
        //session.close();

    }

    @Override
    @Transactional
    public void updateIDF() {
        Long numRes = resourceService.countResources();
        List <Dictionary> dictionaries = getAll();
        //List <Dictionary> updateDict = new ArrayList<>();
        for (Dictionary dictionary : dictionaries){
            dictionary.setIdf(Math.log((double)numRes/dictionary.getResources_count()));
            dictRepository.save(dictionary);
            //updateDict.add(dictionary);
        }
        //dictRepository.saveAll(updateDict);
    }

    @Override
    public Long getIdByWord(String word) {
        return dictRepository.getIdByWord(word);
    }

}
