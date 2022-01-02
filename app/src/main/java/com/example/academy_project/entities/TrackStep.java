package com.example.academy_project.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "TrackSteps")
public class TrackStep {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    private int id;
    @ColumnInfo(name = "courseId")
    private int courseId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "steps")
    private List<Step> steps;

    @Ignore
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
