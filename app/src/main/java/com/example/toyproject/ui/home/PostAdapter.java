package com.example.toyproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toyproject.PostDetailActivity;
import com.example.toyproject.R;
import com.example.toyproject.model.PostListResponse;
import java.util.List;

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

        // 제목이 17자를 초과할 경우, 뒤에 "..."을 추가
        String title = post.getTitle();
        if (title.length() > 17) {
            title = title.substring(0, 17) + "...";
        }

        // 내용이 36자를 초과할 경우, 뒤에 "..."을 추가
        String content = post.getContent();
        if (content.length() > 36) {
            content = content.substring(0, 36) + "...";
        }

        holder.titleTextView.setText(title);
        holder.contentTextView.setText(content);

        // 아이템 뷰 클릭 이벤트 설정
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, PostDetailActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.postTitle);
            contentTextView = itemView.findViewById(R.id.postContent);
        }
    }
}
