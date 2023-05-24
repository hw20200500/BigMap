package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class board_write extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        db = FirebaseFirestore.getInstance();
        

        EditText edit_view_title = findViewById(R.id.view_title);
        EditText edit_View_Text = findViewById(R.id.editText);
        Button free_reg_Button = findViewById(R.id.reg);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        free_reg_Button.setOnClickListener(v -> {
            String view_title = edit_view_title.getText().toString();
            String view_Text = edit_View_Text.getText().toString();

            if (view_title.isEmpty()) {
                Toast.makeText(getApplicationContext(), "제목을 작성해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (view_Text.isEmpty()) {
                Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = user.getUid();

            // 현재 시간 가져오기
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(calendar.getTime());

            HashMap<String, Object> post = new HashMap<>();
            post.put("제목", view_title);
            post.put("내용", view_Text);
            post.put("작성자", userId);
            post.put("작성 시간/날짜", time);

            db.collection("게시판DB")
                    .document()
                    .set(post)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(board_write.this, "글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(board_write.this, board_free.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(board_write.this, "글을 등록하지 못했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(board_write.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
