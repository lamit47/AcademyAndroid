package com.example.academy_project.apis;

import com.example.academy_project.entities.Category;
import com.example.academy_project.entities.ChangePassword;
import com.example.academy_project.entities.Comment;
import com.example.academy_project.entities.Course;
import com.example.academy_project.entities.CourseStep;
import com.example.academy_project.entities.EditInfo;
import com.example.academy_project.entities.Question;
import com.example.academy_project.entities.TrackStep;
import com.example.academy_project.entities.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/User/Me")
    Call<User> getUser();

    @GET("api/Course")
    Call<List<Course>> getListCourse();

    @GET("api/Category")
    Call<List<Category>> getListCategory();

    @GET("api/Course/{courseId}/TrackSteps")
    Call<List<TrackStep>> getTrackStep(@Path(value = "courseId", encoded = true) String courseId);

    @GET("api/Question")
    Call<List<Question>> getListQuestions();

    @GET("api/Question/{Id}")
    Call<Question> getQuestion(@Path(value = "Id", encoded = true) String Id);

    @GET("api/Question/{Id}/Comments")
    Call<List<Comment>> getComments(@Path(value = "Id", encoded = true) String Id);

    @POST("api/Answer")
    Call<Comment> postComment(@Body Comment comment);

    @POST("api/Question")
    Call<Question> postQuestion(@Body Question question);

    @PUT("api/Question/{Id}")
    Call<Question> putQuestion(@Path(value = "Id", encoded = true) String Id,@Body Question question);

    @PUT("api/Answer/{Id}")
    Call<Comment> putComment(@Path(value = "Id", encoded = true) String Id,@Body Comment comment);

    @DELETE("api/Answer/{Id}")
    Call<ResponseBody> deleteComment(@Path(value = "Id", encoded = true) String Id);

    @GET("api/Step/{Id}")
    Call<CourseStep> getStep(@Path(value = "Id", encoded = true) String Id);

    @PUT("api/User/Infomation")
    Call<EditInfo> putInfo(@Body EditInfo editInfo);

    @POST("api/User/Password")
    Call<ResponseBody> postNewPassword(@Body ChangePassword changePassword);

    @Multipart
    @POST("api/Picture/Profile")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
