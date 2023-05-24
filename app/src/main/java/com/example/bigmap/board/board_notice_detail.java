package com.example.bigmap.board;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skt.tmap.engine.navigation.network.NetworkRequester;



import com.example.bigmap.R;

public class board_notice_detail extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_notice_detail);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    Button btn_comment_reg = findViewById(R.id.btn_comment_reg);




}
