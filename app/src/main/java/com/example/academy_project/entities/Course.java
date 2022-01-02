package com.example.academy_project.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Courses")
public class Course {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    private int id;
    @Ignore
    private int lecturerId;
    @Ignore
    private int categoryId;
    @Ignore
    private int pictureId;
    @ColumnInfo(name = "title")
    private String title;
    @Ignore
    private String description;
    private int credits;
    @Ignore
    private Date createdAt;
    @Ignore
    private Date updatedAt;
    @Ignore
    private boolean isDeleted;
    @ColumnInfo(name = "picturePath")
    private String picturePath;
    @Ignore
    private double progress;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", lecturerId=" + lecturerId +
                ", categoryId=" + categoryId +
                ", pictureId=" + pictureId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", credits=" + credits +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                ", picturePath='" + picturePath + '\'' +
                ", progress=" + progress +
                '}';
    }

    public Course() {

    }

    public Course(int id, int lecturerId, int categoryId, int pictureId, String title, String description, int credits, Date createdAt, Date updatedAt, boolean isDeleted, String picturePath, double progress) {
        this.id = id;
        this.lecturerId = lecturerId;
        this.categoryId = categoryId;
        this.pictureId = pictureId;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.picturePath = picturePath;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCredits() {
        return credits;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public double getProgress() {
        return progress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}

