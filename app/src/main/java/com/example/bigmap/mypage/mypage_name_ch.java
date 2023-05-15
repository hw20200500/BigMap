package com.example.bigmap.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class mypage_name_ch extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_name_ch);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        // user에 현재 로그인되어있는 사용자 정보 저장
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        EditText editname = findViewById(R.id.userName);
        Button button_save = findViewById(R.id.button_save);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editname.getText().length() != 0) {
                    String name = editname.getText().toString();
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


                    DocumentReference docref=firestore.collection("사용자DB").document(email);
                    docref.update("name", name);

                    Toast.makeText(mypage_name_ch.this, "이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mypage_name_ch.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}