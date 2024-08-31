package com.example.toyproject.api;

import com.example.toyproject.model.EmailRequest;
import com.example.toyproject.model.LoginRequest;
import com.example.toyproject.model.LoginResponse;
import com.example.toyproject.model.PostRequestBody;
import com.example.toyproject.model.SignUpRequest;
import com.example.toyproject.model.VerifyEmailRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/mail/mail-send")
    Call<Void> sendVerificationCode(@Body EmailRequest emailRequest);

    @POST("/api/auth/signup")
    Call<Void> signUp(@Body SignUpRequest signUpRequest);

    @POST("/api/mail/mail-check")
    Call<Boolean> verifyEmailCode(@Body VerifyEmailRequest verifyEmailRequest);

    @POST("/api/auth/login")
    Call<Void> login(@Body LoginRequest loginRequest);

    @POST("/api/board/post")
    Call<Void> post(@Header("Authorization") String token, @Body PostRequestBody postRequestBody);
}
