package com.example.bluegreen.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityLoginBinding;
import com.example.bluegreen.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends BaseActivity {

    ActivityProfileBinding binding;
    Button updateProfileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gọi phương thức để tải dữ liệu từ session và Firebase
        setInfomationFromSession();
        UpdateInfor();
        backMainMenu();
    }

    private void backMainMenu() {
        binding.backBtn.setOnClickListener(v -> {
            startActivity(new Intent(Profile.this, MainActivity.class));
        });
    }

    private void setInfomationFromSession() {
        // Lấy email từ session
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Anonymous");

        // Tham chiếu tới Firebase Realtime Database
        DatabaseReference myRef = database.getReference("User");

        // Tạo truy vấn để tìm người dùng dựa trên email
        Query query = myRef.orderByChild("email").equalTo(email);

        // Thêm ValueEventListener để lấy dữ liệu
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Lấy dữ liệu người dùng từ snapshot
                        String id = userSnapshot.child("id").getValue(String.class);
                        String name = userSnapshot.child("name").getValue(String.class);
                        String address = userSnapshot.child("address").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);

                        // Cập nhật giao diện với dữ liệu người dùng
                        binding.profileEmail.setText(email);
                        binding.profileName.setText(name != null ? name : "Anonymous");
                        binding.profileAddress.setText(address != null ? address : "Anonymous");
                        binding.profilePhone.setText(phone != null ? phone : "Anonymous");
                    }
                } else {
                    // Người dùng không tồn tại, xử lý lỗi tại đây
                    Log.d("Firebase", "User with email " + email + " not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi truy vấn bị hủy
                Log.d("Firebase", "Error getting user data: ", databaseError.toException());
            }
        });
    }
    private void UpdateInfor(){
        binding.updateProfileButton.setOnClickListener(v -> {
            Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            String dtoName = binding.profileName.getText().toString();
            String dtoAddress = binding.profileAddress.getText().toString();
            String dtoPhone = binding.profilePhone.getText().toString();
            DatabaseReference myRef = database.getReference("User");
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "Anonymous");
            // Tạo một Map chứa dữ liệu cần cập nhật
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("name", dtoName);
            userUpdates.put("address", dtoAddress);
            userUpdates.put("phone", dtoPhone);

            // Cập nhật dữ liệu vào Firebase dựa trên email
            myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            userSnapshot.getRef().updateChildren(userUpdates);

                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", dtoName);
                        editor.putString("address", dtoAddress);
                        editor.putString("phone", dtoPhone);
                        editor.apply();
                        Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi khi truy vấn bị hủy
                    Log.d("Firebase", "Error updating user data: ", databaseError.toException());
                }
            });


        });
    }
}