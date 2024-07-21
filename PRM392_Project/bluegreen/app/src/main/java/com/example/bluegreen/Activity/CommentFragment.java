package com.example.bluegreen.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bluegreen.Adapter.CommentAdapter;
import com.example.bluegreen.Domain.Comment;
import com.example.bluegreen.Domain.Foods;
import com.example.bluegreen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CommentFragment extends Fragment {

    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Foods object = null;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        if (getArguments() != null) {
            object = (Foods) getArguments().getSerializable("object");
        }

        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList, getActivity(), this::deleteComment);
        commentsRecyclerView.setAdapter(commentAdapter);

        loadComments();

        EditText commentEditText = view.findViewById(R.id.commentEditText);
        Button sendCommentButton = view.findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(v -> {
            String content = commentEditText.getText().toString();
            if (!content.isEmpty()) {
                addComment(content);
                commentEditText.setText("");
            }
        });

        return view;
    }

    private void loadComments() {

        GetCommentDataFromFirebase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Comment> list = task.getResult();
                for (Comment comment : list) {
                    if ((object.getId()+"").equals(comment.getProductId())) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }
        });
    }
    private void addComment(String content) {
        // Thêm bình luận mới vào danh sách và cập nhật giao diện
        String timestamp =  new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Anonymous");

        Comment comment = new Comment(UUID.randomUUID().toString(), email, object.getId()+"", content, timestamp);
        commentList.add(comment);
        commentAdapter.notifyItemInserted(commentList.size() - 1);

        SaveData(comment);
    }
    private void deleteComment(int position) {
        // Xác định bình luận cần xóa từ danh sách
        Comment deletedComment = commentList.get(position);

        // Xóa bình luận khỏi danh sách
        commentList.remove(position);

        // Cập nhật giao diện
        commentAdapter.notifyItemRemoved(position);

        // Xóa dữ liệu từ Firebase
        deleteComment(deletedComment.getId());
    }

    private void SaveData(Comment comment){
        String childValue = comment.getId();

        //Save data to Firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Comment").child(childValue)
                .setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Comment saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to save Comment", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private Task<ArrayList<Comment>> GetCommentDataFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Comment");
        TaskCompletionSource<ArrayList<Comment>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Comment> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(Comment.class));
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

    private void deleteComment(String commentId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Comment").child(commentId);

        databaseRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Comment deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to delete comment: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
