package com.example.bigmap.login_register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_pwfind extends AppCompatActivity {

    EditText editEmail;
    EditText editName;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pwfind);


        Button bttn_findpw = findViewById(R.id.bttn_findpw);
        editEmail = findViewById(R.id.userEmail);
        editName = findViewById(R.id.userName);

        bttn_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String name = editName.getText().toString();

                DocumentReference docRef = firestore.collection("사용자DB").document(email);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String db_name = document.getString("name");
                                if (db_name.equals(name)) {
                                    String pw = document.getString("password");
                                    Log.d(TAG, "비밀번호: "+pw);
                                    Intent foundpw = new Intent(Login_pwfind.this, foundPw.class);
                                    foundpw.putExtra("password", pw);
                                    startActivity(foundpw);
                                }else {
                                    Log.d(TAG, "이름이 잘못되었습니다.");
                                    Toast.makeText(Login_pwfind.this, "이메일이나 이름이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Log.d(TAG, "이메일이나 이름이 잘못되었습니다.");
                                Toast.makeText(Login_pwfind.this, "이메일이나 이름이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "회원정보가 존재하지 않습니다.");
                            Toast.makeText(Login_pwfind.this, "회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    public void findid(View view) {
        Intent intent_pw = new Intent(Login_pwfind.this, Login_idfind.class);
        startActivity(intent_pw);
    }
}