package com.example.toyproject.api;

import com.example.toyproject.model.ChangePasswordRequest;
import com.example.toyproject.model.DeleteUserRequest;
import com.example.toyproject.model.EmailRequest;
import com.example.toyproject.model.LoginRequest;
import com.example.toyproject.model.MyPageResponse;
import com.example.toyproject.model.PostListResponse;
import com.example.toyproject.model.PostRequestBody;
import com.example.toyproject.model.SignUpRequest;
import com.example.toyproject.model.VerifyEmailRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/api/board/lists")
    Call<List<PostListResponse>> getPostList();

    @POST("api/auth/logout")
    Call<Void> logOut(@Header("Authorization") String token);

    @GET("/api/mypage/profile")
    Call<MyPageResponse> myProfile(@Header("Authorization") String token);

    @HTTP(method = "DELETE", path = "/api/mypage/delete-user", hasBody = true)
    Call<Boolean> deleteUser(@Header("Authorization") String token, @Body DeleteUserRequest deleteUserRequest);

    @DELETE("/api/board/{id}")
    Call<Void> deletePost(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("/api/mypage/changePassword")
    Call<Void> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest changPasswordRequest);

    @GET("/api/board/{postId}/likes/count")
    Call<Integer> getPostLikes(@Path("postId") Long postId);
}
