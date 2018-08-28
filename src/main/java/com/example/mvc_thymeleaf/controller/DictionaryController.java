package com.example.mvc_thymeleaf.controller;
import com.example.mvc_thymeleaf.entity.Course;
import com.example.mvc_thymeleaf.entity.Dictionary;
import com.example.mvc_thymeleaf.entity.Discipline;
import com.example.mvc_thymeleaf.entity.FileFormat;
import com.example.mvc_thymeleaf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@Controller
public class DictionaryController {

    @Autowired
    private DictionaryService dictService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private FileFormatService fileFormatService;

    @RequestMapping("/")
    public String search(Model model) {
        model.addAttribute("mess", "Hello!");
        return "search";
    }

    @RequestMapping(value = "/showTerms", method = RequestMethod.GET)
    public String getDictionaryPage(@RequestParam ("pageDict") Optional<Integer> pageDict,  Model model) {
        int pageW = (pageDict.orElse(0) < 1) ? 0 : pageDict.get() - 1;
        int currentPageW = pageW + 1;
        model.addAttribute("startPageW", currentPageW);
        Page<Dictionary> dictionaryPage = dictService.getPage(pageW);
        int iW = 0,  totalW = dictionaryPage.getTotalPages();
		if(currentPageW >= totalW){
			dictionaryPage = dictService.getPage(totalW - 1);
		}
        else if(currentPageW == totalW - 1){
            iW = 1;
        }
        else {
            iW = 2;
        }
        model.addAttribute("iW", iW);
        model.addAttribute("countPagesW", totalW);
        model.addAttribute("listDictionary", dictionaryPage.getContent());

        List<Course> courses = courseService.getAll();
        model.addAttribute("listCourses", courses);

        List<Discipline> disciplines = disciplineService.getAll();
        model.addAttribute("listDisciplines", disciplines);

        List<FileFormat> fileformats = fileFormatService.getAll();
        model.addAttribute("listFileFormats", fileformats);

        return "dictionaryPage";
    }

}
