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
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationCode(String email) {
        EmailRequest emailRequest = new EmailRequest(email);

        Call<Boolean> call = apiService.sendVerificationCode(emailRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean success = response.body();
                    if (success != null && success) {
                        Toast.makeText(RegisterActivity.this, "인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "인증 코드 발송 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "서버 응답 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Network request failed", t); // 예외 출력
            }
        });

    }
}