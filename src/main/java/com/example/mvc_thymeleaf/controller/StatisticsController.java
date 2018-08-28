package com.example.mvc_thymeleaf.controller;

import com.example.mvc_thymeleaf.entity.*;
import com.example.mvc_thymeleaf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {
    private Long id_op;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SemanticClusterService semanticClusterService;
    @Autowired
    private ExtraStatisticsService extraStatisticsService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private ExtraJournalService extraJournalService;

    @RequestMapping("/calculateStatistics")
    public @ResponseBody String calculateStatistics() {
        statisticsService.deleteAll();
        extraStatisticsService.deleteAll();
        Journal startLog = new Journal();
        startLog.setDate(new Date());
        startLog.setOperation("Подсчет статистики");
        startLog.setStatus("В процессе");
        journalService.saveLog(startLog);
        List<Resource> docs = resourceService.getAll();
        Map<String, Double> key_dictionary;
        Map<String, Double> key_dictionary1 = new HashMap<>();
        for(Resource res : docs){
            ExtraJournal ej = new ExtraJournal();
            ej.setDate(new Date());
            ej.setOperation("Resource #" + res.getId_resource()+": Анализ текста документа");
            extraJournalService.saveLog(ej);
            id_op = ej.getId();
            List<String> documentText = resourceService.extractDocumentText(res.getPath());
            ExtraJournal ej1 = new ExtraJournal();
            ej1.setDate(new Date());
            ej1.setOperation("Подсчет термов");
            extraJournalService.saveLog(ej1);
            key_dictionary = resourceService.calculateTerms(documentText);
            ExtraJournal ej2 = new ExtraJournal();
            ej2.setDate(new Date());
            ej2.setOperation("Сортировка термов");
            extraJournalService.saveLog(ej2);
            key_dictionary = resourceService.sortKeyWords(key_dictionary);
            ExtraJournal ej3 = new ExtraJournal();
            ej3.setDate(new Date());
            ej3.setOperation("Определение ключевых термов");
            extraJournalService.saveLog(ej3);
            Double limit_size_map = key_dictionary.size() * 0.3;
            int limit = limit_size_map.intValue();
            key_dictionary = resourceService.removeInsignificantTerms(key_dictionary, limit);
            ExtraJournal ej5 = new ExtraJournal();
            ej5.setDate(new Date());
            ej5.setOperation("Подсчет TF");
            extraJournalService.saveLog(ej5);
            resourceService.calculateTermsTF(key_dictionary);
            ExtraJournal ej6 = new ExtraJournal();
            ej6.setDate(new Date());
            ej6.setOperation("Подсчет весовых коэффициентов");
            extraJournalService.saveLog(ej6);
            statisticsService.calculateTermsWeight(key_dictionary);
            ExtraJournal ej7 = new ExtraJournal();
            ej7.setDate(new Date());
            ej7.setOperation("Сортировка термов");
            extraJournalService.saveLog(ej7);
            key_dictionary = resourceService.sortKeyWords(key_dictionary);
            ExtraJournal ej8 = new ExtraJournal();
            ej8.setDate(new Date());
            ej8.setOperation("Определение ключевых термов");
            extraJournalService.saveLog(ej8);
            limit_size_map = key_dictionary.size() * 0.3;
            limit = limit_size_map.intValue();
            key_dictionary = resourceService.removeInsignificantTerms(key_dictionary, limit);
            ExtraJournal ej9 = new ExtraJournal();
            ej9.setDate(new Date());
            ej9.setOperation("Обновление поисковой статистики");
            extraJournalService.saveLog(ej9);
            statisticsService.updateWeight(key_dictionary, res);
            key_dictionary.clear();
            key_dictionary1.clear();
        }
        ExtraJournal ej10 = new ExtraJournal();
        ej10.setDate(new Date());
        ej10.setOperation("Обновление статуса обработки");
        extraJournalService.saveLog(ej10);
        resourceService.updateStatus();
        Journal endLog = new Journal();
        endLog.setDate(new Date());
        endLog.setOperation("Подсчет статистики");
        endLog.setStatus("Завершено");
        journalService.saveLog(endLog);
        return "Завершено";
    }

    @RequestMapping(value = "/showProcessingStat", method = RequestMethod.GET)
    public @ResponseBody List<ExtraJournal> showProcessingStat() {
        List<ExtraJournal> listik = extraJournalService.getNewRecords(id_op);
        return listik;
    }

    @RequestMapping(value = "/showJournalStat", method = RequestMethod.GET)
    public @ResponseBody List<Journal> showJournalStat() {
        List<Journal> listik1 = journalService.sortJournal();
        return listik1;
    }

    @RequestMapping(value = "/runSearch", method = RequestMethod.POST)
    public String result_search(@RequestParam String query, Model model) {
        model.addAttribute("search_query", query);
        List<String> parse_query = Arrays.asList(query.split(" "));
        Map<String, Double> tf_words = resourceService.calculateTerms(parse_query);

        List<String> extend_words = new ArrayList<>();
        for (Map.Entry<String, Double> e_word : tf_words.entrySet()) {
            String query_word = e_word.getKey();
            extend_words.add(query_word);
            extend_words.addAll(semanticClusterService.extendQueryWord(query_word));
        }

        double sum_log = 0.0, total_sum = 0.0;
        Map<Long,Double> classification_weight = new HashMap<>();
        for(Discipline discipline: disciplineService.getAll()) {
            Long id_discipline = discipline.getId_discipline();
            Long numDiscWords = extraStatisticsService.countDisciplineWords(id_discipline);
            if(numDiscWords == null){
                continue;
            }
            for (Map.Entry<String, Double> e_word : tf_words.entrySet()) {
                String query_word = e_word.getKey();
                Double numDiscWord = extraStatisticsService.countDisciplineWord(dictionaryService.getIdByWord(query_word), id_discipline);
                if(numDiscWord == null){
                    numDiscWord = 0.0;
                }
                double Pw_c = (numDiscWord + 1.0) / (numDiscWords + dictionaryService.countWords());
                sum_log += Math.log(Pw_c);
            }
            double disc_c= (double)resourceService.countResourcesByDiscipline(id_discipline);
            double res_c= (double)resourceService.countResources();
            sum_log += Math.log( disc_c / res_c);
            classification_weight.put(id_discipline,sum_log);
            //total_sum += Math.exp(sum_log);
            sum_log = 0.0;
        }
        /*for (Map.Entry<Long, Double> elem_class : classification_weight.entrySet()) {
            double new_weight = Math.exp(elem_class.getValue()) / total_sum;
            classification_weight.put(elem_class.getKey(), new_weight);
        }*/
        classification_weight = semanticClusterService.sortWordWeight(classification_weight);
        List<Long> res_disciplines = classification_weight.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList()).subList(0,3);
        try {
            if (!extend_words.isEmpty()) {
                List<SearchReport> results_search = statisticsService.calculateRelevance(extend_words, res_disciplines);
                if (!results_search.isEmpty()) {
                    Map<Double, Map<Long, Double>> res_map = new TreeMap<>(Collections.reverseOrder());
                    for(Map.Entry<Long, Double> disc: classification_weight.entrySet()){
                        res_map.put(disc.getValue(), new HashMap<>());
                        for (SearchReport sr : results_search){
                            if(sr.getResource().getDiscipline().getId_discipline().equals(disc.getKey())) {
                                res_map.get(disc.getValue()).put(sr.getResource().getId_resource(), sr.getRelevance());
                            }
                        }
                    }
                    List<Resource> end_res = new ArrayList<>();
                    for(Map.Entry<Double, Map<Long, Double>> elem_map: res_map.entrySet()){
                        Map<Long, Double> resource_map = elem_map.getValue();
                        resource_map = semanticClusterService.sortWordWeight(resource_map);
                        for (Map.Entry<Long, Double> e_res : resource_map.entrySet()) {
                            Resource res = resourceService.getById(e_res.getKey());
                            end_res.add(res);
                        }
                    }
                    /*Map<Long, Double> res_map = new HashMap<>();
                    double sum_rel = 0.0;
                    for (SearchReport sr : results_search) {
                        sum_rel += sr.getRelevance();

                    }
                    for (SearchReport sr : results_search) {
                        Long id_disc = sr.getResource().getDiscipline().getId_discipline();
                        double current_relevance = sr.getRelevance();
                        if(current_relevance == 0.0){
                            continue;
                        }
                        double class_weight = classification_weight.get(id_disc);
                        double new_relevance = current_relevance * class_weight;
                        sr.setRelevance(new_relevance);
                        res_map.put(sr.getResource().getId_resource(), sr.getRelevance());
                    }*/

                    //res_map = semanticClusterService.sortWordWeight(res_map);
                    model.addAttribute("map_search", end_res);
                    return "result_search";
                } else {
                    model.addAttribute("empty_result", "Ничего не найдено!");
                    return "result_search";
                }

            } else {
                model.addAttribute("empty_result", "Ничего не найдено!");
                return "result_search";
            }
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("empty_result", "Ничего не найдено!");
            return "result_search";
        }
    }

    @RequestMapping(value = "/showStatistics", method = RequestMethod.GET)
    public String getStatisticPage(@RequestParam("pageNumber") Optional<Integer> pageNumber, Model model) {
        /*int page = (pageNumber.orElse(0) < 1) ? 0 : pageNumber.get() - 1;
        int currentPage = page + 1;
        model.addAttribute("startPage", currentPage);
        Page<Statistics> statisticPage = statisticsService.getPage(page);
        int i = 0,  total = statisticPage.getTotalPages();
        if(currentPage >= total){
            statisticPage = statisticsService.getPage(total - 1);
        }
        else if(currentPage == total - 1){
            i = 1;
        }
        else {
            i = 2;
        }

        model.addAttribute("i", i);
        model.addAttribute("countPages", total);*/
        List<Statistics> statistics = statisticsService.getAll();
        //model.addAttribute("listStatistics", statisticPage.getContent());
        model.addAttribute("listStatistics", statistics);
        List<ExtraStatistics> extraStatistics = extraStatisticsService.getAll();
        model.addAttribute("listExtraStatistics", extraStatistics);
        return "statisticPage";
    }
}