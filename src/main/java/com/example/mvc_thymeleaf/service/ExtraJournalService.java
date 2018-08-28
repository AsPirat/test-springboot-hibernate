package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.ExtraJournal;

import java.util.List;

public interface ExtraJournalService {

    List<ExtraJournal> getAll();
    ExtraJournal saveLog(ExtraJournal extrajournal);
    List<ExtraJournal> getNewRecords(Long id_oper);

}
