package com.example.toyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.ui.home.WriteFragment;
import com.example.toyproject.utils.PreferenceManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", -1);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String authorUsername = intent.getStringExtra("authorUsername");
        String createAt = dateformat(intent.getStringExtra("createAt"));
        String postLikes = intent.getStringExtra("postLikes");

        TextView titleView = findViewById(R.id.title);
        TextView contentView = findViewById(R.id.content);
        TextView authorUsernameView = findViewById(R.id.authorUsername);
        TextView createAtView = findViewById(R.id.createAt);
        TextView postDetailLikesView = findViewById(R.id.postDetailLikes);

        titleView.setText(title);
        contentView.setText(content);
        authorUsernameView.setText(authorUsername);
        createAtView.setText(createAt);
        postDetailLikesView.setText(postLikes);

        ImageView goBackButton = findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> {
            finish();
        });

        ImageView postDetailHamburger = findViewById(R.id.postDetailHamburger);
        postDetailHamburger.setOnClickListener(v -> showPopupMenu(v, id, title, content));
    }

    private String dateformat(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        ZonedDateTime utcZonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String formattedDate = kstZonedDateTime.format(formatter);

        return formattedDate;
    }

    private void showPopupMenu(View view, Long id, String title, String content) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.post_detail_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            return onMenuItemClick(item, id, title, content);
        });
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item, Long postId, String title, String content) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("id", postId);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("NavigateTo", "edit");
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            deletePost(postId);
            return true;
        } else {
            return false;
        }
    }

    private void deletePost(Long id) {
        String token = PreferenceManager.getAccessTokenKey(this);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deletePost("Bearer " + token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostDetailActivity.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostDetailActivity.this, "본 게시글에 대한 삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}