package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue(generator ="journal_seq")
    @GenericGenerator(
            name = "journal_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "journal_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private Date date;
    private String operation;
    private String status;

    public Journal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
