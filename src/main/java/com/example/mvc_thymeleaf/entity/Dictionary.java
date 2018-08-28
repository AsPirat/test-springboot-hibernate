package com.example.mvc_thymeleaf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="dictionary")
public class Dictionary implements Serializable{

    @Id
    @GeneratedValue(generator ="dictionary_seq")
    @GenericGenerator(
            name = "dictionary_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "dictionary_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private Long resources_count;

    private Double idf;

    private String key_word;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "first_dict")
    private Set<SemanticCluster> cluster_main;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "second_dict")
    private Set<SemanticCluster> cluster_link;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stat_dict")
    private Set<Statistics> stats;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dict")
    private Set<ExtraStatistics> extra_stats;

    public Dictionary(Long resources_count, Double idf, String key_word) {
        this.resources_count = resources_count;
        this.idf = idf;
        this.key_word = key_word;
    }

    public Dictionary() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResources_count() {
        return resources_count;
    }

    public void setResources_count(Long resources_count) {
        this.resources_count = resources_count;
    }

    public Double getIdf() {
        return idf;
    }

    public void setIdf(Double idf) {
        this.idf = idf;
    }

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }

    public Set<SemanticCluster> getCluster_main() {
        return cluster_main;
    }

    public void setCluster_main(Set<SemanticCluster> cluster_main) {
        this.cluster_main = cluster_main;
    }

    public Set<SemanticCluster> getCluster_link() {
        return cluster_link;
    }

    public void setCluster_link(Set<SemanticCluster> cluster_link) {
        this.cluster_link = cluster_link;
    }

    public Set<Statistics> getStats() {
        return stats;
    }

    public void setStats(Set<Statistics> stats) {
        this.stats = stats;
    }

    public Set<ExtraStatistics> getExtra_stats() {
        return extra_stats;
    }

    public void setExtra_stats(Set<ExtraStatistics> extra_stats) {
        this.extra_stats = extra_stats;
    }
}