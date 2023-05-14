package com.example.bigmap.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class mypage_pw_check extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_pw_check);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        EditText userPassword = findViewById(R.id.userPassword);

        Button button_ok = findViewById(R.id.button_ok);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_password = userPassword.getText().toString();
                // user에 현재 로그인되어있는 사용자 정보 저장
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
                String email = user.getEmail();
                // 파이어스토어 데이터베이스 가져오는 코드
                DocumentReference docRef = firestore.collection("사용자DB").document(email);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //각 'field'에 해당하는 데이터 값 가져오기
                                String pw = document.getString("password");

                                if (current_password.equals(pw)) {
                                    Toast.makeText(mypage_pw_check.this, "비밀번호 확인", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mypage_pw_check.this, mypage_pw_ch.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(mypage_pw_check.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }
}