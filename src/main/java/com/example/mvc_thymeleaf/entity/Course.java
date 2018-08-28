package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @GeneratedValue(generator ="course_seq")
    @GenericGenerator(
            name = "course_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "course_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id_course;

    private String course;

    private String code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private Set<Resource> resources;

    public Course() {
    }

    public Long getId_course() {
        return id_course;
    }

    public void setId_course(Long id_course) {
        this.id_course = id_course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}
