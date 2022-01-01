package com.example.academy_project.apis;

import com.example.academy_project.entities.Comment;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.CourseStep;
import com.example.academy_project.entities.EditInfo;
import com.example.academy_project.entities.Question;
import com.example.academy_project.entities.Step;
import com.example.academy_project.entities.TrackStep;
import com.example.academy_project.entities.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/User/Me")
    Call<User> getUser();

    @GET("api/Course")
    Call<List<Course>> getListCourse();

    @GET("api/Course/{courseId}/TrackSteps")
    Call<List<TrackStep>> getTrackStep(@Path(value = "courseId", encoded = true) String courseId);

    @GET("api/Question")
    Call<List<Question>> getListQuestions();

    @GET("api/Question/{Id}")
    Call<Question> getQuestion(@Path(value = "Id", encoded = true) String Id);

    @GET("api/Question/{Id}/Comments")
    Call<List<Comment>> getComments(@Path(value = "Id", encoded = true) String Id);

    @POST("api/Answer")
    Call<Comment> postComent(@Body Comment comment);

    @PUT("api/Answer/{Id}")
    Call<Comment> putComment(@Path(value = "Id", encoded = true) String Id,@Body Comment comment);

    @DELETE("api/Answer/{Id}")
    Call<ResponseBody> DeletedComment(@Path(value = "Id", encoded = true) String Id);

    @GET("api/Step/{Id}")
    Call<CourseStep> getStep(@Path(value = "Id", encoded = true) String Id);

    @PUT("api/User/Information")
    Call<EditInfo> putInfo(@Body EditInfo editInfo);
}
