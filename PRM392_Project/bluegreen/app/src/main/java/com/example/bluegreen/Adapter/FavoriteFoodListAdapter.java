package com.example.bluegreen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bluegreen.Activity.DetailActivity;
import com.example.bluegreen.Domain.Favorite;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFoodListAdapter extends RecyclerView.Adapter<FavoriteFoodListAdapter.ViewHolder> {
    private ArrayList<Foods> items;
    private Context context;
    private DatabaseReference favoritesRef;
    private String userEmail;

    public FavoriteFoodListAdapter(ArrayList<Foods> items, String userEmail) {
        this.items = items;
        this.userEmail = userEmail;
        favoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorite");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_favorite_food, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.timeTxt.setText(items.get(position).getTimeValue() + " min");
        holder.priceTxt.setText("$" + items.get(position).getPrice());
        holder.rateTxt.setText("" + items.get(position).getStar());

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
        holder.unfavoriteBtn.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition(); // Get current adapter position

            // Ensure currentPosition is valid
            if (currentPosition != RecyclerView.NO_POSITION) {
                Foods food = items.get(currentPosition); // Get food item at current position
                double foodId = food.getId(); // Get foodId from the Foods object

                // Remove favorite from Firebase
                Query query = favoritesRef.orderByChild("foodId").equalTo(foodId);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot issue : snapshot.getChildren()) {
                                Favorite favorite = issue.getValue(Favorite.class);
                                if (favorite != null && favorite.getEmail().equals(userEmail)) {
                                    issue.getRef().removeValue(); // Remove the favorite entry from Firebase
                                    items.remove(currentPosition); // Remove from local list
                                    notifyItemRemoved(currentPosition); // Notify adapter
                                    notifyDataSetChanged(); // Notify data set changed
                                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                    return; // Exit after removing the first matching favorite
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, rateTxt, timeTxt;
        ImageView pic, unfavoriteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            rateTxt = itemView.findViewById(R.id.rateTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            pic = itemView.findViewById(R.id.img);
            unfavoriteBtn = itemView.findViewById(R.id.unfavoriteBtn);
        }
    }
}