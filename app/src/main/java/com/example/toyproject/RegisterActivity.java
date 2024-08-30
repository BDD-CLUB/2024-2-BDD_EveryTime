package com.example.toyproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.model.EmailRequest;
import com.example.toyproject.model.SignUpRequest;
import com.example.toyproject.model.VerifyEmailRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ApiService 초기화
        apiService = RetrofitClient.getClient().create(ApiService.class);

        Button goBackToLogin = findViewById(R.id.goBackToLogin);

        goBackToLogin.setOnClickListener(v -> {
            finish();
        });

        Button registerButton = findViewById(R.id.registerButton);
        EditText registerName = findViewById(R.id.registerName);
        EditText registerEmail = findViewById(R.id.registerEmail);
        Button sendEmailCode = findViewById(R.id.sendEmailCode);
        EditText registerEmailCode = findViewById(R.id.registerEmailCode);
        EditText registerPassword = findViewById(R.id.registerPassword);

        sendEmailCode.setOnClickListener(v -> {
            String email = registerEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                sendVerificationCode(email);
            } else {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            String username = registerName.getText().toString().trim();
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String emailCode = registerEmailCode.getText().toString().trim();

            if (!emailCode.isEmpty()) {
                verifyEmailCode(email, emailCode, username, password);
            } else {
                Toast.makeText(this, "이메일 인증코드를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 이메일 인증 코드 전송 메서드
    private void sendVerificationCode(String email) {
        EmailRequest emailRequest = new EmailRequest(email);

        // Retrofit을 이용해 서버에 요청 전송
        Call<Void> call = apiService.sendVerificationCode(emailRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // HTTP 200 상태 코드일 때
                    Toast.makeText(RegisterActivity.this, "인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Toast.makeText(RegisterActivity.this, "서버 응답 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류 처리
                Toast.makeText(RegisterActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Network request failed", t);
            }
        });
    }

    // 회원가입 메서드
    private void signUp(String username, String password, String email) {
        SignUpRequest signUpRequest = new SignUpRequest(username, password, email);

        Call<Void> call = apiService.signUp(signUpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // HTTP 200 상태 코드일 때
                    Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Toast.makeText(RegisterActivity.this, "서버 응답 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류 처리
                Toast.makeText(RegisterActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Network request failed", t);
            }
        });
    }

    // 인증코드 확인 메서드
    private void verifyEmailCode(String email, String verifyCode, String username, String password) {
        VerifyEmailRequest verifyEmailRequest = new VerifyEmailRequest(email, verifyCode);

        Call<Boolean> call = apiService.verifyEmailCode(verifyEmailRequest);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean isVerified = response.body();
                    if (isVerified != null && isVerified) {
                        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                            signUp(username, password, email);
                        } else {
                            Toast.makeText(RegisterActivity.this, "이름, 이메일, 비밀번호를 모두 기입해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "이메일 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "서버 응답 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}