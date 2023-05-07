package com.example.bigmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class board_free extends AppCompatActivity {

    // writeButton을 id를 이용하여 가져옵니다.
    private Button writeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_free);

        // writeButton을 id를 이용하여 가져옵니다.
        writeButton = findViewById(R.id.write);

        // writeButton에 클릭 이벤트를 등록합니다.
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BoardWriteActivity 로 화면을 전환합니다.
                Intent intent = new Intent(board_free.this, board_write.class);
                startActivity(intent);
            }
        });
    }


}