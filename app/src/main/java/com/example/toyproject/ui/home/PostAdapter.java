package com.example.toyproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toyproject.PostDetailActivity;
import com.example.toyproject.R;
import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.model.PostListResponse;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostListResponse> posts;

    public PostAdapter(List<PostListResponse> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_post.xml을 inflate하여 ViewHolder를 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        // 현재 위치의 게시글 데이터를 가져와서 ViewHolder에 바인딩
        PostListResponse post = posts.get(position);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Integer> call = apiService.getPostLikes(post.getId());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer count = response.body();
                    holder.postLikesTextView.setText(String.valueOf(count));

                    // 아이템 뷰 클릭 이벤트 설정
                    holder.itemView.setOnClickListener(v -> {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra("title", post.getTitle());
                        intent.putExtra("content", post.getContent());
                        intent.putExtra("id", post.getId());
                        intent.putExtra("authorUsername", post.getAuthorUsername());
                        intent.putExtra("createAt", post.getCreateAt());
                        intent.putExtra("postLikes", String.valueOf(count));

                        context.startActivity(intent);
                    });
                } else {
                    Toast.makeText(holder.itemView.getContext(), "좋아요 개수 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(holder.itemView.getContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

        // 제목이 17자를 초과할 경우, 뒤에 "..."을 추가
        String title = post.getTitle();
        String shortTitle = title;
        if (title.length() > 17) {
            shortTitle = title.substring(0, 17) + "...";
        }

        // 내용이 36자를 초과할 경우, 뒤에 "..."을 추가
        String content = post.getContent();
        String shortContent = content;
        if (content.length() > 36) {
            shortContent = content.substring(0, 36) + "...";
        }

        holder.titleTextView.setText(shortTitle);
        holder.contentTextView.setText(shortContent);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView postLikesTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.postTitle);
            contentTextView = itemView.findViewById(R.id.postContent);
            postLikesTextView = itemView.findViewById(R.id.postLikes);
        }
    }
}
