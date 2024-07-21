package com.example.bluegreen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bluegreen.Adapter.FavoriteFoodListAdapter;
import com.example.bluegreen.Domain.Favorite;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityFavoriteFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFoodsActivity extends AppCompatActivity {
    ActivityFavoriteFoodsBinding binding;
    private RecyclerView.Adapter adapterFavoriteFood;
    private ArrayList<Favorite> favoriteFoodsListNew;
    private ArrayList<Foods> listFoodFavorite;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance().getReference();

        getIntentExtra();
        initFavoriteList();
    }

    private void initFavoriteList() {
        DatabaseReference favorites = database.child("Favorite");

        // Get the current user's email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("email", "Anonymous");

        binding.progressBar.setVisibility(View.VISIBLE);
        favoriteFoodsListNew = new ArrayList<>();

        // Query the favorites for the current user
        Query query = favorites.orderByChild("email").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        favoriteFoodsListNew.add(issue.getValue(Favorite.class));
                    }
                    if (favoriteFoodsListNew.size() > 0) {
                        listFoodFavorite = new ArrayList<>();
                        for (Favorite favorite : favoriteFoodsListNew) {
                            DatabaseReference foodRef = database.child("Foods").child(String.valueOf(favorite.getFoodId()));
                            foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                                    Foods food = foodSnapshot.getValue(Foods.class);
                                    listFoodFavorite.add(food);
                                    if (listFoodFavorite.size() == favoriteFoodsListNew.size()) {
                                        binding.favoriteFoodsView.setLayoutManager(new GridLayoutManager(FavoriteFoodsActivity.this, 2));
                                        adapterFavoriteFood = new FavoriteFoodListAdapter(listFoodFavorite,username);
                                        binding.favoriteFoodsView.setAdapter(adapterFavoriteFood);
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(FavoriteFoodsActivity.this, "Failed to load favorite foods", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(FavoriteFoodsActivity.this, "No favorite foods found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(FavoriteFoodsActivity.this, "No favorite foods found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(FavoriteFoodsActivity.this, "Failed to load favorite foods", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIntentExtra() {
        binding.backBtn.setOnClickListener(v -> finish());
    }



}