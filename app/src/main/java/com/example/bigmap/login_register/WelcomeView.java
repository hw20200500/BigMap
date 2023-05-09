package com.example.bigmap.login_register;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.R;

public class WelcomeView extends AppCompatActivity {

    //회원가입을 완료한 뒤에 나오는 환영 페이지
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_view);
    }
}