package com.example.bluegreen.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bluegreen.Adapter.CartAdapter;
import com.example.bluegreen.Domain.Coupon;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.Domain.MD5Util;
import com.example.bluegreen.Domain.User;
import com.example.bluegreen.Domain.Utils;
import com.example.bluegreen.Helper.JavaMailAPI;
import com.example.bluegreen.Helper.ManagmentCart;
import com.example.bluegreen.Helper.NotificationService;
import com.example.bluegreen.databinding.ActivityCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax = 0;
    private double discount;
    private double subtotal,total;
    private String couponCode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initList();
        PlaceOrder();
        ApplyCoupon();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.1; //percent 2% tax
        double delivery = 20; // 20 Dollar

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;

        subtotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
        double discountAmount = Math.round((this.discount/100) * subtotal * 100)/100;
        total = Math.round((managmentCart.getTotalFee() + tax + delivery - discountAmount) * 100) / 100;

        binding.totalFeeTxt.setText("$" + subtotal + "+");
        binding.taxTxt.setText("$" + tax+ "+");
        binding.deliveryTxt.setText("$" + delivery+ "+");
        binding.discountTxt.setText("$" + discountAmount+ "-");
        binding.totalTxt.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        //Lay data tu session
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String address = sharedPreferences.getString("address", "Anonymous");
        String phone = sharedPreferences.getString("phone", "Anonymous");
        binding.addressTxt.setText(address.toString());
        binding.phoneTxt.setText(phone.toString());
    }

    private void PlaceOrder(){
        binding.PlaceOrderBtn.setOnClickListener(v -> {
            if (managmentCart.getListCart().isEmpty()) {
                Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else if(binding.addressTxt.getText().toString().isEmpty() || binding.phoneTxt.getText().toString().isEmpty()){
                Toast.makeText(CartActivity.this, "Please enter Receiver Address and Phone", Toast.LENGTH_SHORT).show();
            }
            else {
                StringBuilder strProduct = new StringBuilder();
                for (Foods food : managmentCart.getListCart()) {
                    strProduct.append("<tr>");
                    strProduct.append("<td>").append(food.getTitle()).append("</td>");
                    strProduct.append("<td>").append(food.getNumberInCart()).append("</td>");
                    strProduct.append("<td>$").append(food.getPrice()).append("</td>");
                    strProduct.append("</tr>");
                }

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "Anonymous");
                String name = sharedPreferences.getString("name", "Anonymous");
                String receiverAddress = binding.addressTxt.getText().toString();
                String receiverPhone = binding.phoneTxt.getText().toString();
                Random rd = new Random();
                StringBuilder orderCode = new StringBuilder("ORD-");
                for (int i = 0; i < 9; i++) {
                    orderCode.append(rd.nextInt(10));
                }

                Utils utils = new Utils();

                //Create context and send mail to admin
                String contextEmail = utils.getAdminEmailContent(orderCode.toString()
                                            ,name
                                            ,LocalDateTime.now().toLocalDate().toString()
                                            ,strProduct.toString()
                                            ,"$" + String.format("%.2f", subtotal)
                                            ,"$" +String.format("%.2f", total)
                                            ,receiverAddress
                                            ,receiverPhone
                                            ,email);
                JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, utils.ADMIN_Email,utils.ADMIN_SUBJECT, contextEmail);
                javaMailAPI1.execute();

                String contextCustomerEmail = utils.getCustomerEmailContent(orderCode.toString()
                        ,name
                        ,LocalDateTime.now().toLocalDate().toString()
                        ,strProduct.toString()
                        ,"$" + String.format("%.2f", subtotal)
                        ,"$" +String.format("%.2f", total)
                        ,receiverAddress
                        ,receiverPhone
                        ,email);
                JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, email,utils.ADMIN_SUBJECT, contextCustomerEmail);
                javaMailAPI2.execute();

                UpdateCoupon(couponCode);

                managmentCart.emptyListCart();

                Toast.makeText(CartActivity.this, "Payment completed.", Toast.LENGTH_SHORT).show();

                NotificationService.displayNotification(this, "BlueGreen Food", "Đơn hàng của bạn được đặt thành công.");
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });
    }

    private void ApplyCoupon(){
        binding.couponBtn.setOnClickListener(v -> {
            String couponCode = binding.couponTxt.getText().toString().trim();

            SetDiscountValue(couponCode).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    calculateCart();
                }
            });
        });
    }

    private Task<ArrayList<Coupon>> GetCouponFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Coupon");
        TaskCompletionSource<ArrayList<Coupon>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Coupon> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(Coupon.class));
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

    private Task<Void> SetDiscountValue(String couponCode) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        GetCouponFromFirebase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Coupon> list = task.getResult();
                Coupon couponData = null;
                for (Coupon coupon : list) {
                    // Kiểm tra thông tin mã coupon từ dữ liệu Firebase
                    if (coupon.getCode().equals(couponCode)) {
                        couponData = coupon;
                        break;
                    }
                }

                if (couponData != null) {

                   try{
                       //So sanh ngay ton tai cua coupon
                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                       LocalDate startDate = LocalDate.parse(couponData.getStartDate(), formatter);
                       LocalDate endDate = LocalDate.parse(couponData.getEndDate(), formatter);
                       LocalDate currentDate = LocalDate.now();

                       if(startDate.isBefore(currentDate) && currentDate.isBefore(endDate)){
                           this.discount = couponData.getDiscount();
                           this.couponCode = couponData.getCode();
                           Toast.makeText(CartActivity.this, "Apply Discount " + couponData.getDiscount() + "% to Subtotal Price.", Toast.LENGTH_SHORT).show();
                           taskCompletionSource.setResult(null);
                       }else{
                           Toast.makeText(CartActivity.this, "Invalid Coupon.", Toast.LENGTH_SHORT).show();
                       }
                   }catch (Exception e){
                       Toast.makeText(CartActivity.this, "Error when getting coupon", Toast.LENGTH_SHORT).show();
                   }

                } else {
                    this.discount = 0;
                    Toast.makeText(CartActivity.this, "Invalid Coupon.", Toast.LENGTH_SHORT).show();
                    taskCompletionSource.setResult(null);
                }
            } else {
                Toast.makeText(CartActivity.this, "Error when getting coupon", Toast.LENGTH_SHORT).show();
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }

    private void UpdateCoupon(String couponCode) {
        GetCouponFromFirebase().addOnCompleteListener(task -> {
            ArrayList<Coupon> list = task.getResult();
            Coupon couponData = null;
            for (Coupon coupon : list) {
                // Kiểm tra thông tin mã coupon từ dữ liệu Firebase
                if (coupon.getCode().equals(couponCode)) {
                    couponData = coupon;
                    break;
                }
            }

            if (couponData != null){
                Map<String, Object> couponUpdates = new HashMap<>();
                couponUpdates.put("total", couponData.getTotal()-1);
                String childValue = couponData.getId();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Coupon").child(childValue).updateChildren(couponUpdates);
            }
        });
    }
}