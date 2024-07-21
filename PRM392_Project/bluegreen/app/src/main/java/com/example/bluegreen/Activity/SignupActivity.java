package com.example.bluegreen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bluegreen.Domain.MD5Util;
import com.example.bluegreen.Domain.User;
import com.example.bluegreen.Domain.Utils;
import com.example.bluegreen.Helper.JavaMailAPI;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SignUp();
        MoveToLoginScreen();
        SendOTP();
    }
    private void SignUp() {
        binding.signupBtn.setOnClickListener(v -> {
            //check has input uncorrect otp or not
            if (binding.OTP.getText().toString().isEmpty() || !CheckValidOTP()) {
                Toast.makeText(SignupActivity.this, "Please enter correct OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                //get data from form
                String email = binding.userEdt.getText().toString().toLowerCase();
                String password = binding.passEdt.getText().toString();
                String confirmPassword = binding.confirmPassEdt.getText().toString();

                //check valid password
                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Your password must be 6 character.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!confirmPassword.equals(password)){
                    Toast.makeText(SignupActivity.this, "Password must equal with confirm password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check Email exist or not
                checkEmailExists(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean exists = task.getResult();
                        if (exists) {
                            Toast.makeText(SignupActivity.this, "Email already used.", Toast.LENGTH_SHORT).show();
                        } else {
                            SaveData(email, password);
                            DeleteSession();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Unknown error happened!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void DeleteSession(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("currentTimeMillis");
        editor.remove("otpSignUpCode");
        editor.apply();
    }
    private void MoveToLoginScreen(){
        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
    private boolean isTimeElapsed(long savedTimeMillis, int type) {
        // Get current Time
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();

        // Check is selapsed time is still valid or not
        long elapsedTimeMillis = currentTimeMillis - savedTimeMillis;

        //For send OTP time is 30s = 30000, for OTP valid time is 10p = 600000
        // 1 is otp send time, 2 is otp valid time
        if(type == 1)
            //if elapsedTime > 30000 => Valid time to resend OTP => Else false
            return !(elapsedTimeMillis > 30000);
        else if(type == 2)
            //if elapsedTime > 600000 => OTP has expired  => true
            return elapsedTimeMillis > 600000;
        else
            return  true;
    }
    private void SendOTP(){
        Utils utils = new Utils();
        //Set session to check OTP
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        binding.sendOTPBtn.setOnClickListener(v -> {
            if(!binding.userEdt.getText().toString().isEmpty()){
                long OTPSendTime = sharedPreferences.getLong("currentTimeMillis", 0);
                if(!isTimeElapsed(OTPSendTime,1)){
                    try {
                        String OTP = utils.generateOTP();
                        //Send mail to email user input
                        JavaMailAPI javaMailAPI = new JavaMailAPI(this, binding.userEdt.getText().toString(),utils.SUBJECT, utils.getEmailContentWithOTP(OTP));
                        javaMailAPI.execute();

                        //Add otp code and time
                        editor.putString("otpSignUpCode", OTP);
                        editor.putLong("currentTimeMillis", Calendar.getInstance().getTimeInMillis());
                        editor.apply();

                        //Show message
                        Toast.makeText(SignupActivity.this, "Send mail Succsessfully.", Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        Toast.makeText(SignupActivity.this, "Send mail Failed.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupActivity.this, "Please try again after 30s.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(SignupActivity.this, "Please enter email to send OTP.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean CheckValidOTP(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String otp = sharedPreferences.getString("otpSignUpCode", "");
        long time = sharedPreferences.getLong("currentTimeMillis", Calendar.getInstance().getTimeInMillis());

        if(otp.isEmpty() || isTimeElapsed(time, 2)){
            return false;
        }
        return  true;
    }
    private Task<Boolean> checkEmailExists(String email) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        GetDataFromFirebase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> list = task.getResult();
                boolean isValidUser = false;
                for (User user : list) {
                    // Kiểm tra thông tin email từ dữ liệu Firebase
                    if (user.getEmail().equals(email)) {
                        isValidUser = true;
                        break;
                    }
                }
                taskCompletionSource.setResult(isValidUser);
            } else {
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
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
    private Task<Integer> GetCountFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
        TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount(); // Đếm số lượng phần tử trong DataSnapshot
                taskCompletionSource.setResult(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }
    private void SaveData(String email, String password){
        GetCountFromFirebase().addOnCompleteListener(task -> {
            String childValue = "0";
            childValue = (task.getResult() + 1) + "";
            User user = new User(childValue, email, MD5Util.md5(password), "", "", "", false);

            //Save data to Firebase
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("User").child(childValue)
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed to save user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}