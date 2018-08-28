package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "discipline")
public class Discipline implements Serializable {
    @Id
    @GeneratedValue(generator ="discipline_seq")
    @GenericGenerator(
            name = "discipline_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "discipline_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id_discipline;

    private String discipline;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discipline")
    private Set<Resource> resources;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "disc")
    private Set<ExtraStatistics> extra_stats;

    public Discipline() {
    }

    public Long getId_discipline() {
        return id_discipline;
    }

    public void setId_discipline(Long id_discipline) {
        this.id_discipline = id_discipline;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<ExtraStatistics> getExtra_stats() {
        return extra_stats;
    }

    public void setExtra_stats(Set<ExtraStatistics> extra_stats) {
        this.extra_stats = extra_stats;
    }
}