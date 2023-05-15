package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annotation.NonNull;
import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skt.tmap.engine.navigation.network.NetworkRequester;


import java.util.HashMap;

public class board_write extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        EditText edit_view_title = findViewById(R.id.view_title);
        EditText edit_View_Text = findViewById(R.id.editText);
        Button free_reg_Button = (Button) findViewById(R.id.reg);

        //+작성자, 시간 저장

        free_reg_Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(edit_View_Text.length()!=0&&edit_view_title.length()!=0){
                    free_reg_Button.setEnabled(true);

                    String view_title = edit_View_Text.getText().toString();
                    String view_Text = edit_View_Text.getText().toString();

                    firebaseAuth.newfreeText(view_title, view_Text).addOnCompleteListener(board_write.this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String uid = user.getUid();

                                HashMap<Object, Object> hashMap= new HashMap<>();

                                hashMap.put("view_title", view_title);
                                hashMap.put("view_Text", view_Text);

                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                firestore.collection("게시판DB").document();

                                //글 등록 성공 화면 전환
                                Intent intent = new Intent(board_write.this, board_free.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(board_write.this, "글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(board_write.this, "글을 등록하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });

                    .addOnFailureListener(board_write.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            Toast.makeText(board_write.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else{
                    if (edit_view_title.length()==0){
                        Toast.makeText(getApplicationContext(), "제목을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(edit_View_Text.length()==0){
                            Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

}