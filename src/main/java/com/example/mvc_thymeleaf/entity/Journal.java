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

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "main_journal")
    private Set<ExtraJournal> extra_journals;*/

    public Journal() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*public Set<ExtraJournal> getExtra_journals() {
        return extra_journals;
    }

    public void setExtra_journals(Set<ExtraJournal> extra_journals) {
        this.extra_journals = extra_journals;
    }*/
}
