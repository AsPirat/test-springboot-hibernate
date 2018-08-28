package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Journal;
import com.example.mvc_thymeleaf.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalRepository journalRepository;

    @Override
    public List<Journal> getAll() {
        return journalRepository.findAll();
    }

    @Override
    public Journal saveLog(Journal journal) {
        return journalRepository.save(journal);
    }

    @Override
    public List<Journal> sortJournal() {
        return journalRepository.sortJournal();
    }
}
