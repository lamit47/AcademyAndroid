package com.example.academy_project.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.academy_project.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert
    void insertCourse(Course course);

    @Query("SELECT * FROM Courses ORDER BY id DESC")
    List<Course> getAllCourse();

    @Query("DELETE FROM Courses")
    void nukeCourses();
}
