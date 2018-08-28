package com.example.mvc_thymeleaf.entity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue(generator ="resource_seq")
    @GenericGenerator(
            name = "resource_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "resource_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id_resource;

    private String authors;

    private String title;

    private String date_publish;

    private String annotation;

    private String path;

    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
    private Set<Statistics> stats_res;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_discipline")
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_file_format")
    private FileFormat file_format;

    public Resource() {
    }

    public Long getId_resource() {
        return id_resource;
    }

    public void setId_resource(Long id_resource) {
        this.id_resource = id_resource;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_publish() {
        return date_publish;
    }

    public void setDate_publish(String date_publish) {
        this.date_publish = date_publish;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<Statistics> getStats_res() {
        return stats_res;
    }

    public void setStats_res(Set<Statistics> stats_res) {
        this.stats_res = stats_res;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public FileFormat getFile_format() {
        return file_format;
    }

    public void setFile_format(FileFormat file_format) {
        this.file_format = file_format;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
