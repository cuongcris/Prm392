package com.example.bluegreen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bluegreen.Adapter.FoodListAdapter;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.Domain.Location;
import com.example.bluegreen.Domain.Price;
import com.example.bluegreen.Domain.Time;
import com.example.bluegreen.R;
import com.example.bluegreen.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private ArrayList<Foods> list = new ArrayList<>();
    private ArrayList<Foods> originallist = new ArrayList<>();
    private String searchText;
    private int Location, Time, Price;
    private boolean isSearch;
    private Location selectedLocation;
    private Time selectedTime;
    private Price selectedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList(false);
        setVariable();

        initLocation();
        initTime();
        initPrice();

        locationChage();
        timeChage();
        priceChage();

        search();
    }

    private void setVariable() {

    }

    private void search(){
        binding.searchBtn.setOnClickListener(v -> {
            searchText = binding.searchEdt.getText().toString();
            list.clear();
            originallist.clear();
            initList(true);
        });
    }

    private void initList(boolean currentSearch) {
        DatabaseReference myRef = database.getReference("Foods");
        if(!currentSearch){
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        Query query;
        if (isSearch) {
            query = myRef.orderByChild("Title");
            binding.titleTxt.setText("Search");
            binding.searchEdt.setText(searchText);
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                originallist.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            if (searchText != null && !searchText.isEmpty()) {
                                if (food.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                                    list.add(food);
                                    originallist.add(food);
                                }
                            } else {
                                list.add(food);
                                originallist.add(food);
                            }
                        }
                    }

                    if (!list.isEmpty()) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                // Xử lý lỗi nếu có
            }
        });
    }

    public void listchange(){
        ArrayList<Foods> filteredList = new ArrayList<>(originallist); // Start with the original list

        if (selectedLocation != null) {
            filteredList.removeIf(food -> food.getLocationId() != Location);
        }

        if (selectedTime != null && selectedTime.getId() > -1) {
            filteredList.removeIf(food -> food.getTimeId() != Time);
        }

        if (selectedPrice != null && selectedPrice.getId() > -1) {
            filteredList.removeIf(food -> food.getPriceId() != Price);
        }

        list.clear();
        list.addAll(filteredList);
        adapterListFood.notifyDataSetChanged();
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(ListFoodsActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTime() {
        DatabaseReference myRef = database.getReference("Time");
        ArrayList<Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.add(new Time(-1, "All"));
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(ListFoodsActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.add(new Price(-1, "All"));
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(ListFoodsActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.priceSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void locationChage(){
        Spinner locationSpinner = findViewById(R.id.locationSp);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (Location) parent.getItemAtPosition(position);
                Location = selectedLocation.getId();
                listchange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn (tùy chọn)
            }
        });

    }
    public void timeChage(){
        Spinner timeSpinner = findViewById(R.id.timeSp);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = (Time) parent.getItemAtPosition(position);
                Time = selectedTime.getId();
                listchange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn (tùy chọn)
            }
        });

    }
    public void priceChage(){
        Spinner priceSpinner = findViewById(R.id.priceSp);
        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPrice = (Price) parent.getItemAtPosition(position);
                Price = selectedPrice.getId();
                listchange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn (tùy chọn)
            }
        });

    }

}