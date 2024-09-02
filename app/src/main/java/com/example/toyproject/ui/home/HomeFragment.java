package com.example.toyproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toyproject.PostDetailActivity;
import com.example.toyproject.R;
import com.example.toyproject.RegisterActivity;
import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.databinding.FragmentHomeBinding;
import com.example.toyproject.model.PostListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout writeButton = binding.getRoot().findViewById(R.id.goToWrite);
        writeButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_write);
        });

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPosts();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();  // 프래그먼트가 화면에 다시 나타날 때마다 게시글 목록을 새로고침
    }

    private void loadPosts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<PostListResponse>> call = apiService.getPostList();

        call.enqueue(new Callback<List<PostListResponse>>() {
            @Override
            public void onResponse(Call<List<PostListResponse>> call, Response<List<PostListResponse>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "통과", Toast.LENGTH_SHORT).show();
                    List<PostListResponse> posts = response.body();
                    postAdapter = new PostAdapter(posts);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    Toast.makeText(getContext(), "게시글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostListResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Network request failed", t);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}