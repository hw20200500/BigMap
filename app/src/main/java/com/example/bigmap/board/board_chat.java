package com.example.bigmap.board;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.R;

public class board_chat extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_chat);

        Button sendButton = findViewById(R.id.send_Button);
        EditText editText = findViewById(R.id.edittext);
        TextView chatMessage = findViewById(R.id.chatmessage);

    }

    protected void onStart() {
        super.onStart();
    }
}