package com.example.bigmap.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bigmap.R;

public class foundId extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_id);

        Intent get_intent = getIntent();
        String email = get_intent.getStringExtra("email");

        TextView text_email = findViewById(R.id.text_useremail);
        text_email.setText(email);
    }
    public void go_login(View view) {
        Intent intent_login = new Intent(foundId.this, Login.class);
        startActivity(intent_login);
    }

    public void findpw(View view) {
        Intent intent_pw = new Intent(foundId.this, Login_pwfind.class);
        startActivity(intent_pw);
    }
}