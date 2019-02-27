package com.example.nada.devhires.models;

public class Repository {

    private String name;
    private String html_url;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String gethtml_url() {
        return html_url;
    }

    public void sethtml_url(String html_url) {
        this.html_url = html_url;
    }
}
