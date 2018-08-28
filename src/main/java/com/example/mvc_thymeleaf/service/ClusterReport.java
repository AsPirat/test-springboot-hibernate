package com.example.mvc_thymeleaf.service;

public class ClusterReport {

    private Long id_resource;
    private Double weight;

    public ClusterReport(Long id_resource, Double weight) {
        this.id_resource = id_resource;
        this.weight = weight;
    }

    public Long getId_resource() {
        return id_resource;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
