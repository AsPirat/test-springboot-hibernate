package com.example.mvc_thymeleaf.controller;

import com.example.mvc_thymeleaf.entity.*;
import com.example.mvc_thymeleaf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ResourceController {
    private Long id_op;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private ExtraJournalService extraJournalService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private FileFormatService fileFormatService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("idCourse") Long idCourse,
                             @RequestParam("idDiscipline") Long idDiscipline,
                             @RequestParam("idFileFormat") Long idFileFormat,
                             @ModelAttribute Resource newResource,
                             BindingResult errors) {
        String name = null;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                name = file.getOriginalFilename();
                String rootPath = "/home/masha";
                File dir = new File(rootPath + File.separator + "loadFiles");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                String path = dir.getAbsolutePath() + File.separator + name;
                File uploadedFile = new File(path);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.flush();
                stream.close();

                newResource.setCourse(courseService.getById(idCourse));
                newResource.setDiscipline(disciplineService.getById(idDiscipline));
                newResource.setFile_format(fileFormatService.getById(idFileFormat));
                newResource.setPath(path);
                newResource.setStatus("unprocessed");
                resourceService.addResource(newResource);
                return "redirect:/showResources";

            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/showResources";
            }
        } else {
            return "redirect:/showResources";
        }
    }

    @RequestMapping("/processDocuments")
    public @ResponseBody String processDocuments() {
        Journal startLog = new Journal();
        startLog.setDate(new Date());
        startLog.setOperation("Обработка новых ресурсов");
        startLog.setStatus("В процессе");
        journalService.saveLog(startLog);
        List<String> doc_pathes = resourceService.getNewPathes();
        Map<String, Double> key_dictionary;
        for(String path : doc_pathes){
            ExtraJournal ej = new ExtraJournal();
            ej.setDate(new Date());
            ej.setOperation("Анализ текста документа");
            extraJournalService.saveLog(ej);
            id_op = ej.getId();
            List<String> documentText = resourceService.extractDocumentText(path);
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
            ExtraJournal ej4 = new ExtraJournal();
            ej4.setDate(new Date());
            ej4.setOperation("Обновление словаря");
            extraJournalService.saveLog(ej4);
            dictionaryService.calculateDocsCountTerms(key_dictionary);
            key_dictionary.clear();
        }
        ExtraJournal ej5 = new ExtraJournal();
        ej5.setDate(new Date());
        ej5.setOperation("Обновление IDF");
        extraJournalService.saveLog(ej5);
        dictionaryService.updateIDF();
        Journal endLog = new Journal();
        endLog.setDate(new Date());
        endLog.setOperation("Обработка  ресурсов");
        endLog.setStatus("Завершено");
        journalService.saveLog(endLog);
        return "Завершено";
    }

    @RequestMapping(value = "/showProcessing", method = RequestMethod.GET)
    public @ResponseBody List<ExtraJournal> showProcessing() {
        List<ExtraJournal> listik = extraJournalService.getNewRecords(id_op);
        return listik;
    }

    @RequestMapping(value = "/showMainJournal", method = RequestMethod.GET)
    public @ResponseBody List<Journal> showMainJournal() {
        List<Journal> listik1 = journalService.sortJournal();
        return listik1;
    }

    @RequestMapping(value = "/showResources", method = RequestMethod.GET)
    public String getResourcePage(@RequestParam("pageNumber") Optional<Integer> pageNumber, Model model) {
        int page = (pageNumber.orElse(0) < 1) ? 0 : pageNumber.get() - 1;
        int currentPage = page + 1;
        model.addAttribute("startPage", currentPage);
        Page<Resource> resourcePage = resourceService.getPage(page);
        int i = 0,  total = resourcePage.getTotalPages();
        if(currentPage >= total){
            resourcePage = resourceService.getPage(total - 1);
        }
        else if(currentPage == total - 1){
            i = 1;
        }
        else {
            i = 2;
        }
        model.addAttribute("i", i);
        model.addAttribute("countPages", total);
        model.addAttribute("listResources", resourcePage.getContent());

        List<Course> courses = courseService.getAll();
        model.addAttribute("listCourses", courses);

        List<Discipline> disciplines = disciplineService.getAll();
        model.addAttribute("listDisciplines", disciplines);

        List<FileFormat> fileformats = fileFormatService.getAll();
        model.addAttribute("listFileFormats", fileformats);

        model.addAttribute("newResource", new Resource());
        return "resourcePage";
    }
}
