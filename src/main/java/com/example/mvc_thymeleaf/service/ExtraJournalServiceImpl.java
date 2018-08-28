package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.ExtraJournal;
import com.example.mvc_thymeleaf.repository.ExtraJournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtraJournalServiceImpl implements ExtraJournalService {

    @Autowired
    private ExtraJournalRepository extraJournalRepository;

    @Override
    public List<ExtraJournal> getAll() {
        return extraJournalRepository.findAll();
    }

    @Override
    public ExtraJournal saveLog(ExtraJournal journal) {
        return extraJournalRepository.save(journal);
    }

    @Override
    public List<ExtraJournal> getNewRecords(Long id_oper) {
        return extraJournalRepository.getNewRecords(id_oper);
    }
}
