package com.example.bigmap.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class mypage_email_ch extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_email_ch);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        // user에 현재 로그인되어있는 사용자 정보 저장
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        EditText editemail = findViewById(R.id.userEmail);
        Button button_save = findViewById(R.id.button_save);
        String before_e = email;

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editemail.getText().length() != 0) {
                    String ch_email = editemail.getText().toString();


                    HashMap<Object,Object> hashMap = new HashMap<>();

                    DocumentReference docRef = firestore.collection("사용자DB").document(email);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //각 'field'에 해당하는 데이터 값 가져오기
                                    String uid=user.getUid();
                                    String sch_email = editemail.getText().toString();
                                    String name = document.getString("name");
                                    String phoneNum = document.getString("phone_number");
                                    String userPassword = document.getString("password");
                                    int birthY = document.getLong("birth_year").intValue();
                                    int birthM = document.getLong("birth_month").intValue();
                                    int birthD = document.getLong("birth_day").intValue();
//                        int age = document.getLong("age").intValue();

                                    DocumentReference doc = firestore.collection("사용자DB").document(before_e);
                                    doc.delete();

                                    // 가져온 데이터 화면에 출력하기(이미 있는 텍스트뷰 내용을 해당 데이터 값으로 수정)


                                    hashMap.put("email",ch_email);
                                    hashMap.put("password",userPassword);
                                    hashMap.put("name",name);
                                    hashMap.put("birth_year",birthY);
                                    hashMap.put("birth_month",birthM);
                                    hashMap.put("birth_day",birthD);
                                    hashMap.put("phone_number",phoneNum);
                                    hashMap.put("uid",uid);


                                    firestore.collection("사용자DB").document(ch_email).set(hashMap);


                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mypage_email_ch.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                    user.updateEmail(ch_email);

                    Toast.makeText(mypage_email_ch.this, "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mypage_email_ch.this, MainActivity.class);

                    startActivity(intent);


                }
            }
        });


    }
}