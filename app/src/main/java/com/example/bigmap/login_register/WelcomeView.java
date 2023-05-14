package com.example.bigmap.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.R;

public class WelcomeView extends AppCompatActivity {

    //회원가입을 완료한 뒤에 나오는 환영 페이지
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_view);

        LinearLayout go_login_layout = findViewById(R.id.go_login_layout);
        go_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeView.this, Login.class);
                startActivity(intent);
            }
        });
    }
}