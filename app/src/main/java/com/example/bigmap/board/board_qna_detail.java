package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class board_qna_detail extends AppCompatActivity {

    private TextView titleTextView;
    private TextView userTextView;
    private TextView timeTextView;
    private TextView contentTextView;



    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_qna_detail);

        db = FirebaseFirestore.getInstance();

        titleTextView = findViewById(R.id.view_title);
        userTextView = findViewById(R.id.user);
        timeTextView = findViewById(R.id.time_text_view);
        contentTextView = findViewById(R.id.editText);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        // 게시물 데이터 받아오기
        String postId = getIntent().getStringExtra("postId");
        db.collection("qnaDB")
                .document(postId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("제목");
                        String writeruser = documentSnapshot.getString("작성자");
                        String time = documentSnapshot.getString("작성_시간_날짜");
                        String content = documentSnapshot.getString("내용");

                        titleTextView.setText(title);
                        userTextView.setText(writeruser);
                        timeTextView.setText(time);
                        contentTextView.setText(content);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_qna_detail.this, "게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });


    }


}
