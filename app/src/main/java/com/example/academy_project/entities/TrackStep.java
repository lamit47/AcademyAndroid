package com.example.academy_project.entities;

import java.util.List;

public class TrackStep {

    private int id;
    private int courseId;
    private String title;
    private List<Step> steps;

    public TrackStep() {
    }


    public TrackStep(int id, int courseId, String title, List<Step> steps) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

}
