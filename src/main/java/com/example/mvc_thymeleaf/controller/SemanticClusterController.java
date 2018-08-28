package com.example.mvc_thymeleaf.controller;

import com.example.mvc_thymeleaf.entity.ExtraJournal;
import com.example.mvc_thymeleaf.entity.Journal;
import com.example.mvc_thymeleaf.entity.Resource;
import com.example.mvc_thymeleaf.entity.SemanticCluster;
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

@Controller
public class SemanticClusterController {
    private Long id_op;
    @Autowired
    private SemanticClusterService semanticClusterService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private ExtraJournalService extraJournalService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ExtraStatisticsService extraStatisticsService;

    @RequestMapping("/updateSemanticCluster")
    public @ResponseBody String updateSemanticCluster() {
        //extraStatisticsService.deleteAll();
        semanticClusterService.deleteAll();
        Journal startLog = new Journal();
        startLog.setDate(new Date());
        startLog.setOperation("Обновление семантического кластера");
        startLog.setStatus("В процессе");
        journalService.saveLog(startLog);

        List<Resource> docs = resourceService.getAll();
        Map<String, Double> key_dictionary;
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
            key_dictionary = resourceService.removeInsignificantTerms(key_dictionary, 100);
            ExtraJournal ej4 = new ExtraJournal();
            ej4.setDate(new Date());
            ej4.setOperation("Обновление статистики для обучения классификатора");
            extraJournalService.saveLog(ej4);
            extraStatisticsService.updateTrainingData(key_dictionary, res);
            key_dictionary.clear();

        }
        ExtraJournal ej10 = new ExtraJournal();
        ej10.setDate(new Date());
        ej10.setOperation("Обновление статуса обработки");
        extraJournalService.saveLog(ej10);
        resourceService.updateStatus();

        ExtraJournal ej = new ExtraJournal();
        ej.setDate(new Date());
        ej.setOperation("Обновление термов для расширения запроса");
        extraJournalService.saveLog(ej);
        id_op = ej.getId();
        semanticClusterService.updateSemanticCluster();

        Journal endLog = new Journal();
        endLog.setDate(new Date());
        endLog.setOperation("Обновление семантического кластера");
        endLog.setStatus("Завершено");
        journalService.saveLog(endLog);
        return "Завершено";
    }

    @RequestMapping(value = "/showProcessingCluster", method = RequestMethod.GET)
    public @ResponseBody List<ExtraJournal> showProcessingStat() {
        List<ExtraJournal> listik = extraJournalService.getNewRecords(id_op);
        return listik;
    }

    @RequestMapping(value = "/showJournalCluster", method = RequestMethod.GET)
    public @ResponseBody List<Journal> showJournalStat() {
        List<Journal> listik1 = journalService.sortJournal();
        return listik1;
    }
    @RequestMapping("/Cluster")
    public @ResponseBody List<JsonCluster> viewCluster() {
        List<SemanticCluster> scluster = semanticClusterService.getAll();
        Map<String, Map<String, Double>> aaa = new HashMap<>();
        Map<String, Map<String, Double>> aaa1 = new HashMap<>();
        List<JsonCluster> jcluster = new ArrayList<>();

        for(SemanticCluster sc: scluster){
            String name = sc.getFirst_dict().getKey_word();
            if (aaa.containsKey(name)) {
                aaa.get(name).put(sc.getSecond_dict().getKey_word(), sc.getProximity());

            } else {
                Map<String, Double> p = new HashMap<>();
                p.put(sc.getSecond_dict().getKey_word(), sc.getProximity());
                aaa.put(name, p);
            }
        }
        for(Map.Entry<String, Map<String, Double>> elem_map: aaa.entrySet()){
            JsonCluster jc = new JsonCluster();
            jc.setName(elem_map.getKey());
            jc.setLinks(elem_map.getValue());
            jcluster.add(jc);
        }

        for(SemanticCluster sc: scluster){
            String name = sc.getSecond_dict().getKey_word();
            if (aaa1.containsKey(name)) {
                aaa1.get(name).put(sc.getFirst_dict().getKey_word(), sc.getProximity());

            } else {
                Map<String, Double> p = new HashMap<>();
                p.put(sc.getFirst_dict().getKey_word(), sc.getProximity());
                aaa1.put(name, p);
            }
        }
        for(Map.Entry<String, Map<String, Double>> elem_map: aaa1.entrySet()){
            JsonCluster jc = new JsonCluster();
            jc.setName(elem_map.getKey());
            jc.setLinks(elem_map.getValue());
            jcluster.add(jc);
        }
        return jcluster;
    }

    @RequestMapping(value = "/showCluster", method = RequestMethod.GET)
    public String getClusterPage(@RequestParam("pageNumber") Optional<Integer> pageNumber, Model model) {
        int page = (pageNumber.orElse(0) < 1) ? 0 : pageNumber.get() - 1;
        int currentPage = page + 1;
        model.addAttribute("startPage", currentPage);
        Page<SemanticCluster> clusterPage = semanticClusterService.getPage(page);
        int i = 0,  total = clusterPage.getTotalPages();
        if(currentPage >= total){
            clusterPage = semanticClusterService.getPage(total - 1);
        }
        else if(currentPage == total - 1){
            i = 1;
        }
        else {
            i = 2;
        }

        model.addAttribute("i", i);
        model.addAttribute("countPages", total);
        model.addAttribute("listClusters", clusterPage.getContent());
        return "clusterPage";
    }
}
