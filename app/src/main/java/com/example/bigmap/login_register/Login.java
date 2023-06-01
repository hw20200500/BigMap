package com.example.bigmap.login_register;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.R;
import com.example.bigmap.mapview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText Email, Password;
    Button loginButton, newuserButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // 로그인 이메일, 비번 입력창 변수 저장
        Email = findViewById(R.id.userEmail);
        Password = findViewById(R.id.userPassword);

        // 로그인 버튼, 회원가입 버튼
        loginButton = (Button) findViewById(R.id.button_login); // 로그인
        newuserButton = (Button) findViewById(R.id.button_new_user); // 회원가입

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // 로그인 버튼 클릭
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext에 입력된 이메일과 비번을 String 형태로 변환 및 변수에 저장.
                String userEmail = Email.getText().toString();
                String userPassword = Password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        String email = user.getEmail();
                        DocumentReference docRef = firestore.collection("사용자DB").document(email);
                        Email.setText("");
                        Password.setText("");
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String name = document.getString("name");
                                        Log.d(TAG, "Name: " + name);
                                        Toast.makeText(Login.this, "환영합니다. "+name, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d(TAG, "No such document");
                                        Toast.makeText(Login.this, "No such document", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                    Toast.makeText(Login.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Intent intent = new Intent(Login.this, mapview.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 회원가입 버튼 클릭. 회원가입 페이지로 넘어감.
        newuserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserCheck.class);
                startActivity(intent);
            }
        });

        Button bttn_idfind = findViewById(R.id.button_find_id);
        Button bttn_pwfind = findViewById(R.id.button_find_pw);

        bttn_idfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_id = new Intent(Login.this, Login_idfind.class);
                startActivity(intent_id);
            }
        });

        bttn_pwfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pw = new Intent(Login.this, Login_pwfind.class);
                startActivity(intent_pw);
            }
        });
    }


}