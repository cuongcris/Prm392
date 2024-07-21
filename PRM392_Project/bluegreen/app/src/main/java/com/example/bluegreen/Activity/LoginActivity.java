package com.example.bluegreen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bluegreen.Domain.MD5Util;
import com.example.bluegreen.Domain.Price;
import com.example.bluegreen.Domain.User;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
        MoveToForgotScreen();
        MoveToSignUpScreen();
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString().toLowerCase();
            String password = binding.passEdt.getText().toString();
            CheckLogin(email, password);
        });
    }

    private void MoveToForgotScreen(){
        binding.forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void MoveToSignUpScreen(){
        binding.signUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
    private Task<ArrayList<User>> GetDataFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
        TaskCompletionSource<ArrayList<User>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<User> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(User.class));
                    }
                }
                taskCompletionSource.setResult(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }


    private void CheckLogin(String email, String password) {
        GetDataFromFirebase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> list = task.getResult();
                boolean isValidUser = false;
                User userData = new User();
                for (User user : list) {
                    // Kiểm tra thông tin đăng nhập với dữ liệu từ Firebase
                    if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(MD5Util.md5(password))) {
                        isValidUser = true;
                        userData = user;
                        break;
                    }
                }
                if (isValidUser) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("name", userData.getName());
                    editor.putString("address", userData.getAddress());
                    editor.putString("phone", userData.getPhone());
                    editor.putString("isAdmin", userData.isAdmin() + "");
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Username or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}