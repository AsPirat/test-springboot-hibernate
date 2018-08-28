package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "file_format")
public class FileFormat implements Serializable{
    @Id
    @GeneratedValue(generator ="file_format_seq")
    @GenericGenerator(
            name = "file_format_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "file_format_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id_file_format;

    private String file_format;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file_format")
    private Set<Resource> resources;

    public FileFormat() {
    }

    public Long getId_file_format() {
        return id_file_format;
    }

    public void setId_file_format(Long id_file_format) {
        this.id_file_format = id_file_format;
    }

    public String getFile_format() {
        return file_format;
    }

    public void setFile_format(String file_format) {
        this.file_format = file_format;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}
