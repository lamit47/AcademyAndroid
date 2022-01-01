package com.example.academy_project.entities;

public class EditInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String picture;

    public EditInfo() {
    }

    public EditInfo(String email, String firstName, String lastName, String picture) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
