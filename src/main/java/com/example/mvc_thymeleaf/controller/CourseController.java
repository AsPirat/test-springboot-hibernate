package com.example.mvc_thymeleaf.controller;

import com.example.mvc_thymeleaf.entity.FileFormat;
import com.example.mvc_thymeleaf.entity.Journal;
import com.example.mvc_thymeleaf.service.CourseService;
import com.example.mvc_thymeleaf.service.FileFormatService;
import com.example.mvc_thymeleaf.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private FileFormatService fileFormatService;

    @RequestMapping(value = "/showDate", method = RequestMethod.GET)
    public String showDate(Model model){
        return "addCourse";
    }
    @RequestMapping(value = "/addCourse", method = RequestMethod.GET)
    public @ResponseBody List<Journal> updateCourse() {
        /*Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());*/
        List<Journal> listik = journalService.getAll();
        return listik;
    }
}
