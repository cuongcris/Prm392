package com.example.bluegreen.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bluegreen.Helper.NetworkUtils;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        MoveToLoginScreen();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void MoveToLoginScreen(){
        binding.loginBtn.setOnClickListener(v -> {
            if(checkWifiIsConnect()){
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }else{
                Toast.makeText(this, "Please Connect to Wifi first!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void MoveToSignUpScreen(){
        binding.signupBtn.setOnClickListener(v -> {
            if(checkWifiIsConnect()){
                startActivity(new Intent(IntroActivity.this, SignupActivity.class));
            }else{
                Toast.makeText(this, "Please Connect to Wifi first!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

        MoveToSignUpScreen();
    }

    private boolean checkWifiIsConnect(){
        return NetworkUtils.isWifiConnected(getApplicationContext());
    }
}