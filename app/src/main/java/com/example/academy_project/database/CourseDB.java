package com.example.academy_project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.academy_project.daos.CourseDAO;
import com.example.academy_project.daos.CourseStepDAO;
import com.example.academy_project.daos.TrackStepDAO;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.CourseStep;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;

@Database(entities = {Course.class, TrackStep.class, CourseStep.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CourseDB extends RoomDatabase {
    private static final String DATABASE_NAME = "CoursesDatabase";
    private static CourseDB instance;

    public static synchronized CourseDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, CourseDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CourseDAO courseDAO();
    public abstract TrackStepDAO trackStepDAO();
    public abstract CourseStepDAO courseStepDAO();
}