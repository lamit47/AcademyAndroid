package com.example.academy_project.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;

import java.util.List;

@Dao
public interface TrackStepDAO {
    @Insert
    void insertTrackStep(TrackStep trackStep);

    @Query("SELECT * FROM TrackSteps WHERE courseId = :courseId")
    List<TrackStep> getListTrackStep(int courseId);

    @Query("DELETE FROM TrackSteps WHERE courseId = :courseId")
    void deleteTrackStepByCourseId(int courseId);
}
