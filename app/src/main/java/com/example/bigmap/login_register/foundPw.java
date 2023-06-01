package com.example.bigmap.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bigmap.R;

public class foundPw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_pw);

        Intent get_intent = getIntent();
        String password = get_intent.getStringExtra("password");

        TextView text_pw = findViewById(R.id.text_userpw);
        text_pw.setText(password);

    }

    public void go_login(View view) {
        Intent intent_login = new Intent(foundPw.this, Login.class);
        startActivity(intent_login);
    }

    public void findid(View view) {
        Intent intent_id = new Intent(foundPw.this, Login_idfind.class);
        startActivity(intent_id);
    }
}