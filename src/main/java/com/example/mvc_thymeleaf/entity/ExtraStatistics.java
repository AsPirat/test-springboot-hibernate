package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "extra_statistics")
public class ExtraStatistics {
    @Id
    @GeneratedValue(generator ="extra_statistics_seq")
    @GenericGenerator(
            name = "extra_statistics_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "extra_statistics_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private double count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_key_term")
    private Dictionary dict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_discipline")
    private Discipline disc;

    public ExtraStatistics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public Dictionary getDict() {
        return dict;
    }

    public void setDict(Dictionary dict) {
        this.dict = dict;
    }

    public Discipline getDisc() {
        return disc;
    }

    public void setDisc(Discipline disc) {
        this.disc = disc;
    }
}