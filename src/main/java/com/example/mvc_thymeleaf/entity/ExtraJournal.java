package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "extrajournal")
public class ExtraJournal {
    @Id
    @GeneratedValue(generator ="extra_journal_seq")
    @GenericGenerator(
            name = "extra_journal_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "extra_journal_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private Date date;
    private String operation;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_main_operation")
    private Journal main_journal;*/

    public ExtraJournal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    /*public Journal getMain_journal() {
        return main_journal;
    }

    public void setMain_journal(Journal main_journal) {
        this.main_journal = main_journal;
    }*/
}
