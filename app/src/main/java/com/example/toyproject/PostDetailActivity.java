package com.example.toyproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String authorUsername = intent.getStringExtra("authorUsername");
        String createAt = dateformat(intent.getStringExtra("createAt"));

        TextView titleView = findViewById(R.id.title);
        TextView contentView = findViewById(R.id.content);
        TextView authorUsernameView = findViewById(R.id.authorUsername);
        TextView createAtView = findViewById(R.id.createAt);
        titleView.setText(title);
        contentView.setText(content);
        authorUsernameView.setText(authorUsername);
        createAtView.setText(createAt);

        ImageView goBackButton = findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> {
            finish();
        });
    }

    private String dateformat(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        ZonedDateTime utcZonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String formattedDate = kstZonedDateTime.format(formatter);

        return formattedDate;
    }
}