package com.example.toyproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.model.LoginRequest;
import com.example.toyproject.model.LoginResponse;
import com.example.toyproject.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText loginId = findViewById(R.id.loginId);
        EditText loginPassword = findViewById(R.id.loginPassword);


        Button logInButton = findViewById(R.id.logIn);
        logInButton.setOnClickListener(v -> {
            String id = loginId.getText().toString();
            String pw = loginPassword.getText().toString();

            if (!id.isEmpty() && !pw.isEmpty()) {
                login(id, pw);
            } else {
                Toast.makeText(this, "이메일, 비밀번호를 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String id, String pw) {
        LoginRequest loginRequest = new LoginRequest(id, pw);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.login(loginRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String authorizationHeader = response.headers().get("Authorization");
                    String setCookieHeader = response.headers().get("Set-Cookie");

                    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                        String accessToken = authorizationHeader.substring(7);
                        PreferenceManager.saveAccessToken(LoginActivity.this, accessToken);
                    }

                    if (setCookieHeader != null && setCookieHeader.contains("refresh=")) {
                        int startIndex = setCookieHeader.indexOf("refresh=") + 8;
                        int endIndex = setCookieHeader.indexOf(";", startIndex);
                        if (endIndex == -1) {
                            endIndex = setCookieHeader.length();
                        }
                        String refreshToken = setCookieHeader.substring(startIndex, endIndex);
                        PreferenceManager.saveRefreshToken(LoginActivity.this, refreshToken);
                    }

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}