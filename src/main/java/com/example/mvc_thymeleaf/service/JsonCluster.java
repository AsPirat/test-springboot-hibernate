package com.example.mvc_thymeleaf.service;

import java.util.Map;

public class JsonCluster {
    private String name;
    private Map<String, Double> links;

    public JsonCluster() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Double> links) {
        this.links = links;
    }
}
