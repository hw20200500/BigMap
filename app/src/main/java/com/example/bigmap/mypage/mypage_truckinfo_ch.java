package com.example.bigmap.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.mapview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class mypage_truckinfo_ch extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_truckinfo_ch);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        Spinner spinner_height = findViewById(R.id.user_truck_height);
        Spinner spinner_weight = findViewById(R.id.user_truck_weight);
        Spinner spinner_width = findViewById(R.id.user_truck_width);
        Spinner spinner_length = findViewById(R.id.user_truck_length);

        Button button_save = findViewById(R.id.button_save);

        // user에 현재 로그인되어있는 사용자 정보 저장
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        int height = 420;
        Object[] truck_height = new Object[301];
        for (int i = 0; i <= 300; i++) {
            truck_height[i] = height;
            height--;
        }

        int weight = 2500;
        Object[] truck_weight = new Object[101];

        for (int i = 0; i <= 50; i++) {
            truck_weight[i] = weight;
            weight= weight-50;
        }

        int width = 250;
        Object[] truck_width = new Object[101];

        for (int i = 0; i <= 100; i++) {
            truck_width[i] = width;
            width--;
        }

        int length = 1200;
        Object[] truck_length = new Object[101];

        for (int i = 0; i <= 100; i++) {
            truck_length[i] = length;
            length = length - 10;
        }

        //무게 스피너
        ArrayAdapter<Object> adapter_height = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, truck_height);
        adapter_height.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_height.setAdapter(adapter_height);

        spinner_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("높이");
            }
        });

        //무게 스피너
        ArrayAdapter<Object> adapter_weight = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, truck_weight);
        adapter_weight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_weight.setAdapter(adapter_weight);

        spinner_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("무게");
            }
        });
        //너비 스피너
        ArrayAdapter<Object> adapter_width = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, truck_width);
        adapter_width.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_width.setAdapter(adapter_width);

        spinner_width.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("무게");
            }
        });

        //길이 스피너
        ArrayAdapter<Object> adapter_length = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, truck_length);
        adapter_length.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_length.setAdapter(adapter_length);

        spinner_length.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("길이");
            }
        });

        button_save.setOnClickListener(view -> {
            System.out.println("버튼눌림");
            int user_height = Integer.parseInt(spinner_height.getSelectedItem().toString());
            int user_weight = Integer.parseInt(spinner_weight.getSelectedItem().toString());
            int user_width = Integer.parseInt(spinner_width.getSelectedItem().toString());
            int user_length = Integer.parseInt(spinner_length.getSelectedItem().toString());
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            DocumentReference docref = firestore.collection("화물차DB").document(email);
            docref.update("높이", user_height);
            docref.update("무게", user_weight);
            docref.update("너비", user_width);
            docref.update("길이", user_length);

            Toast.makeText(mypage_truckinfo_ch.this, "화물차 사양이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), mapview.class);
            startActivity(intent);
        });
    }

    public void go_before(View view) {
        Intent intent_before = new Intent(this, mapview.class);
        startActivity(intent_before);
    }

}
