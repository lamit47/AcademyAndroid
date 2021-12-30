package com.example.academy_project.entities;

public class CourseStep {
    private int id;
    private int trackId;
    private String title;
    private int duration;
    private String content;
    private String embedLink;

    public CourseStep() {
    }

    public CourseStep(int id, int trackId, String title, int duration, String content, String embedLink) {
        this.id = id;
        this.trackId = trackId;
        this.title = title;
        this.duration = duration;
        this.content = content;
        this.embedLink = embedLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmbedLink() {
        return embedLink;
    }

    public void setEmbedLink(String embedLink) {
        this.embedLink = embedLink;
    }
}
