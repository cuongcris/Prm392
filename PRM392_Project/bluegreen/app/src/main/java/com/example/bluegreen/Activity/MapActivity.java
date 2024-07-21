package com.example.bluegreen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityLoginBinding;
import com.example.bluegreen.databinding.ActivityMapBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    ActivityMapBinding binding;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MoveToMainScreen();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        //footer
        nav = findViewById(R.id.navbar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(MapActivity.this, MainActivity.class));
                } else if (id == R.id.profile) {
                    startActivity(new Intent(MapActivity.this, Profile.class));
                }else if (id == R.id.location) {
                    startActivity(new Intent(MapActivity.this, MapActivity.class));
                }else if (id == R.id.payment) {
                    startActivity(new Intent(MapActivity.this, CartActivity.class));
                } else if (id == R.id.favorite) {
                    startActivity(new Intent(MapActivity.this, FavoriteFoodsActivity.class));
                } else {
                    Toast.makeText(MapActivity.this, "Unknown Item", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(15.969546047631043, 108.26288686359953);
        googleMap.addMarker(new MarkerOptions().position(location).title("BlueGreenFood"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }

    private void MoveToMainScreen(){
        binding.backBtn.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, MainActivity.class));
        });
    }
}