package com.example.bluegreen.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bluegreen.Domain.Favorite;
import com.example.bluegreen.Helper.NotificationService;
import com.google.firebase.database.DatabaseReference;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.Helper.ManagmentCart;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityDetailBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private Favorite favorite;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        setVariable();
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        // Hiển thị CommentsFragment
        CommentFragment commentsFragment = new CommentFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object", object); // Truyền đối tượng item qua Bundle
        commentsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, commentsFragment);
        fragmentTransaction.commit();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText("$" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num * object.getPrice() + "$"));

        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + " ");
            binding.totalTxt.setText("$" + (num * object.getPrice()));
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText("$" + (num * object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
            NotificationService.displayNotification(this, "BlueGreen Food", object.getTitle() + " đã được thêm vào giỏ hàng của bạn!");
        });

        binding.favBtn.setOnClickListener(v -> {
            favorite = new Favorite();
            favorite.setFoodId(object.getId());

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "Anonymous");
            favorite.setEmail(email);

            checkFavoriteAndAdd(favorite.getFoodId(), favorite.getEmail());
        });
    }

    private void getIntentExtra() {
        favorite = (Favorite) getIntent().getSerializableExtra("favorite");
        object = (Foods) getIntent().getSerializableExtra("object");
    }

    private void checkFavoriteAndAdd(int foodId, String email) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorite");

        Query query = favoritesRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean alreadyExists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favorite existingFavorite = snapshot.getValue(Favorite.class);
                    if (existingFavorite != null && existingFavorite.getFoodId() == foodId) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (alreadyExists) {
                    Toast.makeText(DetailActivity.this, "This food is already in your favorites", Toast.LENGTH_SHORT).show();
                } else {
                    addFavoriteFood(foodId, email);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "Error checking favorite food", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFavoriteFood(int foodId, String email) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorite");

        String key = favoritesRef.push().getKey();
        Favorite favorite = new Favorite();
        favorite.setFoodId(foodId);
        favorite.setEmail(email);

        favoritesRef.child(key).setValue(favorite).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Favorite food added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add favorite food", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
