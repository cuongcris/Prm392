package com.example.bluegreen.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegreen.Domain.Comment;
import com.example.bluegreen.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<com.example.bluegreen.Adapter.CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private Context context;
    private com.example.bluegreen.Adapter.CommentAdapter.CommentDeleteListener commentDeleteListener;

    public interface CommentDeleteListener {
        void onDelete(int position);
    }

    public CommentAdapter(List<Comment> commentList,Context context, com.example.bluegreen.Adapter.CommentAdapter.CommentDeleteListener commentDeleteListener) {
        this.commentList = commentList;
        this.commentDeleteListener = commentDeleteListener;
        this.context = context;
    }

    @NonNull
    @Override
    public com.example.bluegreen.Adapter.CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new com.example.bluegreen.Adapter.CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.bluegreen.Adapter.CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.usernameTextView.setText(comment.getUserEmail());
        holder.contentTextView.setText(comment.getContent());
        holder.timestampTextView.setText(comment.getTimestamp());

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Anonymous");

        // Hiển thị nút xóa nếu bình luận thuộc về người dùng hiện tại
        if (email.equals((comment.getUserEmail()))) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> commentDeleteListener.onDelete(position));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView contentTextView;
        TextView timestampTextView;
        ImageButton deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
