package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Journal;
import java.util.List;

public interface JournalService {

    List<Journal> getAll();
    Journal saveLog(Journal journal);
    List<Journal> sortJournal();

}
