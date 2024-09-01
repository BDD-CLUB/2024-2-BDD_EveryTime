package com.example.toyproject.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.toyproject.LoginActivity;
import com.example.toyproject.R;
import com.example.toyproject.api.ApiService;
import com.example.toyproject.api.RetrofitClient;
import com.example.toyproject.databinding.FragmentNotificationsBinding;
import com.example.toyproject.model.DeleteUserRequest;
import com.example.toyproject.model.MyPageResponse;
import com.example.toyproject.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myProfile();

        Button logOutButton = binding.getRoot().findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(v -> logOut());

        Button deleteUser = binding.getRoot().findViewById(R.id.deleteUser);
        deleteUser.setOnClickListener(v -> showPasswordDialog());
        return root;
    }

    private void showPasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_password_input, null);

        final EditText input = dialogView.findViewById(R.id.editTextPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("회원탈퇴");
        builder.setView(dialogView);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String password = input.getText().toString().trim();
                if (!password.isEmpty()) {
                    //deleteUser(password);
                } else {
                    Toast.makeText(getContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

//    private void deleteUser(String password) {
//        String token = PreferenceManager.getAccessTokenKey(getContext());
//
//        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(password);
//
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        Call<Boolean> call = apiService.deleteUser("Bearer " + token, deleteUserRequest);
//        call.enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                if (response.isSuccessful()) {
//                    Boolean checkPassword = response.body();
//                    if (checkPassword) {
//                        Toast.makeText(getContext(), "탈퇴 처리 되었습니다.", Toast.LENGTH_SHORT).show();
//
//                        PreferenceManager.clearTokens(getContext());
//
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        getActivity().finish();
//                    } else {
//                        Toast.makeText(getContext(), "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "탈퇴 처리에 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Toast.makeText(getContext(), "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void myProfile() {
        String token = PreferenceManager.getAccessTokenKey(getContext());

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<MyPageResponse> call = apiService.myProfile("Bearer " + token);
        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                if (response.isSuccessful()) {
                    MyPageResponse myPageResponse = response.body();

                    String username = myPageResponse.getUsername();
                    String email = myPageResponse.getEmail();

                    TextView usernameTextView = binding.getRoot().findViewById(R.id.myPageUserName);
                    TextView userEmailTextView = binding.getRoot().findViewById(R.id.myPageUserEmail);
                    usernameTextView.setText(username);
                    userEmailTextView.setText(truncateEmail(email));
                }
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String truncateEmail(String email) {
        if (email.length() > 16) {
            return email.substring(0, 16) + "...";
        } else {
            return email;
        }
    }

    private void logOut() {
        String token = PreferenceManager.getAccessTokenKey(getContext());

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.logOut("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                    PreferenceManager.clearTokens(getContext());

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}