package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.FileFormat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileFormatRepository extends JpaRepository<FileFormat, Long> {
}
