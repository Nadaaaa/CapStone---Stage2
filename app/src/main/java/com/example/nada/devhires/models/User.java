package com.example.nada.devhires.models;

public class User {
    private String username;
    private String email;
    private String jobTitle;
    private String company;
    private String phone;
    private String maritalStatus;
    private String bio;
    private String githubUsername;

    public User(){

    }

    public User(String username, String email, String jobTitle, String company, String phone, String maritalStatus, String bio, String githubUsername) {
        this.username = username;
        this.email = email;
        this.jobTitle = jobTitle;
        this.company = company;
        this.phone = phone;
        this.maritalStatus = maritalStatus;
        this.bio = bio;
        this.githubUsername = githubUsername;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGithub_username() {
        return githubUsername;
    }

    public void setGithub_username(String githubUsername) {
        this.githubUsername = githubUsername;
    }
}
