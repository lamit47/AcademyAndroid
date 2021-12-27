package com.example.academy_project.apis;

import com.example.academy_project.entities.Login;
import com.example.academy_project.entities.Register;
import com.example.academy_project.entities.Token;
import com.example.academy_project.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/Auth/Token")
    Call<Token> getToken(@Body Login login);
    @POST("api/Auth/Refresh")
    Call<Token> refreshToken(@Body Token token);
    @POST("api/User/Register")
    Call<User> register(@Body Register register);
    @POST("api/User/Forgot")
    Call<Boolean> forgotPassword(@Body String str);
}
