package com.example.academy_project.database;

import androidx.room.TypeConverter;

import com.example.academy_project.entities.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromStepList(List<Step> step) {
        if (step == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        String json = gson.toJson(step, type);
        return json;
    }

    @TypeConverter
    public List<Step> toStepList(String stepString) {
        if (stepString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        List<Step> list = gson.fromJson(stepString, type);
        return list;
    }
}
