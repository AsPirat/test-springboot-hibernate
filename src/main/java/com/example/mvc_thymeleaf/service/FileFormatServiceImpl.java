package com.example.mvc_thymeleaf.service;
import com.example.mvc_thymeleaf.entity.FileFormat;
import com.example.mvc_thymeleaf.repository.FileFormatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileFormatServiceImpl implements FileFormatService {

    @Autowired
    private FileFormatRepository fileFormatRepository;

    @Override
    public List<FileFormat> getAll() {
        return fileFormatRepository.findAll();
    }

    @Override
    public FileFormat getById(Long id) {
        return fileFormatRepository.getOne(id);
    }
}