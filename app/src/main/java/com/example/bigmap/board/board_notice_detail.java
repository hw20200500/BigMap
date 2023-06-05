package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class board_notice_detail extends AppCompatActivity {

    private TextView titleTextView;
    private TextView userTextView;
    private TextView timeTextView;
    private TextView contentTextView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_notice_detail);

        db = FirebaseFirestore.getInstance();

        titleTextView = findViewById(R.id.view_title);
        userTextView = findViewById(R.id.user);
        timeTextView = findViewById(R.id.time_text_view);
        contentTextView = findViewById(R.id.editText);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        // 게시물 데이터 가져오기
        String postId = getIntent().getStringExtra("postId");
        if (postId != null) {
            db.collection("공지사항DB")
                    .document(postId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString("title");
                            String user = documentSnapshot.getString("user");
                            String time = documentSnapshot.getString("time");
                            String content = documentSnapshot.getString("content");

                            titleTextView.setText(title);
                            userTextView.setText(user);
                            timeTextView.setText(time);
                            contentTextView.setText(content);
                        } else {
                            Toast.makeText(board_notice_detail.this, "게시물이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(board_notice_detail.this, "게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
