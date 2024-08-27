package com.example.toyproject.api;

import com.example.toyproject.model.EmailRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/mail/mail-send")
    Call<Void> sendVerificationCode(@Body EmailRequest emailRequest);
}
