package com.example.mvc_thymeleaf.service;
import com.example.mvc_thymeleaf.entity.FileFormat;
import java.util.List;

public interface FileFormatService {

    List<FileFormat> getAll();
    FileFormat getById(Long id);
}
