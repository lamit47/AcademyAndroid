package com.example.academy_project.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.academy_project.entities.CourseStep;

@Dao
public interface CourseStepDAO {
    @Query("SELECT * FROM CourseSteps WHERE id = :stepId")
    CourseStep selectCourseStep(int stepId);

    @Insert
    void insertCourseStep(CourseStep step);

    @Query("DELETE FROM CourseSteps WHERE id = :stepId")
    void deleteCourseStep(int stepId);
}
