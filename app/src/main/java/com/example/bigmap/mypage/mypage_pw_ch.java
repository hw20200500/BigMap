package com.example.bigmap.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.mapview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class mypage_pw_ch extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_pw_ch);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);


        EditText userpw_check = findViewById(R.id.user_pw_check);
        EditText userpw = findViewById(R.id.userPassword);

        //비밀번호
        TextView pwCProvWorld = findViewById(R.id.pwC_prov_world);

        userpw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userpw.getText().toString().equals(userpw_check.getText().toString())) {
                    pwCProvWorld.setText("비밀번호가 일치합니다.");
                    pwCProvWorld.setTextColor(Color.GREEN);
                }
                else {
                    pwCProvWorld.setText("** 비밀번호가 일치하지 않습니다 **");
                    pwCProvWorld.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(userpw.getText().toString().equals(userpw_check.getText().toString())) {
                    pwCProvWorld.setText("비밀번호가 일치합니다.");
                    pwCProvWorld.setTextColor(Color.GREEN);
                }
                else {
                    pwCProvWorld.setText("** 비밀번호가 일치하지 않습니다 **");
                    pwCProvWorld.setTextColor(Color.RED);
                }
            }
        });
        Button button_save = findViewById(R.id.button_save);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userpw.getText().length() != 0 && userpw_check.getText().length() !=0 && userpw.getText().toString().equals(userpw_check.getText().toString())) {
                    String password = userpw.getText().toString();
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String email = user.getEmail();

                    user.updatePassword(password);


                    DocumentReference docref=firestore.collection("사용자DB").document(email);
                    docref.update("password", password);

                    Toast.makeText(mypage_pw_ch.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mypage_pw_ch.this, mapview.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(mypage_pw_ch.this, "비밀번호 변경에 실패하였습니다. 놓친 부분이 없는지 다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}