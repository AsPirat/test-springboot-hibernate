package com.example.mvc_thymeleaf.entity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(generator ="statistics_seq")
    @GenericGenerator(
            name = "statistics_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "statistics_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_key_term")
    private Dictionary stat_dict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resource")
    private Resource resource;

    public Statistics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Dictionary getStat_dict() {
        return stat_dict;
    }

    public void setStat_dict(Dictionary stat_dict) {
        this.stat_dict = stat_dict;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
