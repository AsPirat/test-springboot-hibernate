package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "semantic_cluster")
public class SemanticCluster implements Serializable{

    @Id
    @GeneratedValue(generator ="cluster_seq")
    @GenericGenerator(
            name = "cluster_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "cluster_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private double proximity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_main_term")
    private Dictionary first_dict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_associated_term")
    private Dictionary second_dict;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double proximity) {
        this.proximity = proximity;
    }

    public Dictionary getFirst_dict() {
        return first_dict;
    }

    public void setFirst_dict(Dictionary first_dict) {
        this.first_dict = first_dict;
    }

    public Dictionary getSecond_dict() {
        return second_dict;
    }

    public void setSecond_dict(Dictionary second_dict) {
        this.second_dict = second_dict;
    }
}
