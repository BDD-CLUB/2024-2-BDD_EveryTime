package com.example.toyproject;

import android.app.WallpaperInfo;
import android.content.Intent;
import android.os.Bundle;

import com.example.toyproject.ui.home.HomeFragment;
import com.example.toyproject.ui.home.WriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.toyproject.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String navigateTo = intent.getStringExtra("NavigateTo");

        BottomNavigationView navView = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);

        if (navigateTo != null && navigateTo.equals("edit")) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", intent.getLongExtra("id", -1));
            bundle.putString("title", intent.getStringExtra("title"));
            bundle.putString("content", intent.getStringExtra("content"));

            navController.navigate(R.id.navigation_write, bundle);
        }

        NavigationUI.setupWithNavController(navView, navController);
    }

}