package com.example.academy_project.entities;

public class Step {
//    "id": 0,
//            "trackId": 0,
//            "title": "string",
//            "duration": 0,
//            "completed": true
    private int id;
    private int trackId;
    private String title;
    private int duration;
    private boolean completed;

    public Step() {
    }

    public Step(int id, int trackId, String title, int duration, boolean completed) {
        this.id = id;
        this.trackId = trackId;
        this.title = title;
        this.duration = duration;
        this.completed = completed;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
