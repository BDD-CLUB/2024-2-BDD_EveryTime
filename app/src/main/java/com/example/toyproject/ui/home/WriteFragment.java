package com.example.toyproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.toyproject.R;
import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.model.PostRequestBody;
import com.example.toyproject.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        EditText postTitle = view.findViewById(R.id.postTitle);
        EditText postContent = view.findViewById(R.id.postContent);
        Button postButton = view.findViewById(R.id.postButton);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Long id = arguments.getLong("id", -1);
            String title = arguments.getString("title");
            String content = arguments.getString("content");

            postTitle.setText(title);
            postContent.setText(content);

            postButton.setOnClickListener(v -> {
                String editTitle = postTitle.getText().toString();
                String editContent = postContent.getText().toString();

                if (!editTitle.isEmpty() && !editContent.isEmpty()) {
                    editing(id, editTitle, editContent);
                } else {
                    Toast.makeText(getContext(), "제목과 내용 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            postButton.setOnClickListener(v -> {
                String title = postTitle.getText().toString();
                String content = postContent.getText().toString();

                if (!title.isEmpty() && !content.isEmpty()) {
                    posting(title, content);
                } else {
                    Toast.makeText(getContext(), "제목과 내용 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void posting(String title, String content) {
        String token = PreferenceManager.getAccessTokenKey(getContext());

        PostRequestBody postRequestBody = new PostRequestBody(title, content);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.post("Bearer " + token, postRequestBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigateUp();
                } else {
                    Toast.makeText(getContext(), "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editing(Long id,String title, String content) {
        PostRequestBody postRequestBody = new PostRequestBody(title, content);

        String token = PreferenceManager.getAccessTokenKey(getContext());
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Boolean> call = apiService.editPost("Bearer " + token, id, postRequestBody);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean check = response.body();
                    if (check) {
                        Toast.makeText(getContext(), "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigateUp();
                    } else {
                        Toast.makeText(getContext(), "게시글 수정 권한이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "게시글 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
