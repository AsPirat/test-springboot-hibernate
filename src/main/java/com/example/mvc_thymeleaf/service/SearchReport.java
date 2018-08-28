package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Resource;

public class SearchReport {

    private Resource resource;
    private Double relevance;

    public SearchReport(Resource resource, Double relevance) {
        this.resource = resource;
        this.relevance = relevance;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Double getRelevance() {
        return relevance;
    }

    public void setRelevance(Double relevance) {
        this.relevance = relevance;
    }
}
