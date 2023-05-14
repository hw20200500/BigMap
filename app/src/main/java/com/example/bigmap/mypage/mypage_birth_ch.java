package com.example.bigmap.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.login_register.Login;
import com.example.bigmap.login_register.PersonIform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class mypage_birth_ch extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_birth_ch);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        Spinner spinnerY = findViewById(R.id.userBirth_year);
        Spinner spinnerM = findViewById(R.id.userBirth_month);
        Spinner spinnerD = findViewById(R.id.userBirth_day);

        Button button_save = findViewById(R.id.button_save);

        // user에 현재 로그인되어있는 사용자 정보 저장
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        int Y = 2023;
        Object[] BirthY = new Object[101];
        for (int i = 1; i <= 100; i++) {
            BirthY[i] = Y;
            Y--;
        }

        int M = 1;
        Object[] BirthM = new Object[13];
        for (int i = 1; i <= 12; i++) {
            BirthM[i] = M;
            M++;
        }

        int D = 1;
        Object[] BirthD = new Object[32];
        for (int i = 1; i <= 31; i++) {
            BirthD[i] = D;
            D++;
        }

        ArrayAdapter<Object> adapterY = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthY);
        adapterY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerY.setAdapter(adapterY);

        spinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("연도");
            }
        });

//월 스피너
        ArrayAdapter<Object> adapterM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthM);
        spinnerM.setAdapter(adapterM);

        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("월");
            }
        });

//일 스피너
        ArrayAdapter<Object> adapterD = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthD);
        spinnerD.setAdapter(adapterD);

        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("일");
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerD.getSelectedItemPosition() != 0 && spinnerM.getSelectedItemPosition() != 0 && spinnerY.getSelectedItemPosition() != 0) {
                    int userBirth_year = Integer.parseInt(spinnerY.getSelectedItem().toString());
                    int userBirth_month = Integer.parseInt(spinnerM.getSelectedItem().toString());
                    int userBirth_day = Integer.parseInt(spinnerD.getSelectedItem().toString());
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    DocumentReference docRef = firestore.collection("사용자DB").document(email);


                    DocumentReference docref=firestore.collection("사용자DB").document(email);
                    docref.update("birth_year", userBirth_year);
                    docref.update("birth_month", userBirth_month);
                    docref.update("birth_day", userBirth_day);

                    Toast.makeText(mypage_birth_ch.this, "생년월일이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mypage_birth_ch.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });
    }

    private void go_before() {
        Intent intent_before = new Intent(this, MainActivity.class);
        startActivity(intent_before);
    }
}