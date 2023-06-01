package com.example.bigmap.login_register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class Login_idfind extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private int certif_result=0;
    private String verificationId;
    private String generatedVerificationCode;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Button bttn_findid;
    EditText editUserName;
    EditText editUserPhoneNum;
    EditText editPhoneCertifNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_idfind);


        editUserName = findViewById(R.id.userName);
        editUserPhoneNum = findViewById(R.id.userPhoneNum);
        editPhoneCertifNum = findViewById(R.id.phoneCertif_Num);

        editUserPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        Button buttonSend = (Button) findViewById(R.id.button_num_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPhoneNum = editUserPhoneNum.getText().toString().trim();
                // '-' 제거
                String mWithoutDash = userPhoneNum.replace("-", "");
                Random random = new Random();
                int randomNumber = random.nextInt(999999);

                // 인증번호 생성
                generatedVerificationCode = String.valueOf(randomNumber);

                String sms = "[BigMap]\n전화번호 인증코드 : "+generatedVerificationCode;
                // SMS 전송 권한 확인
                if (ContextCompat.checkSelfPermission(Login_idfind.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(Login_idfind.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            SMS_PERMISSION_REQUEST_CODE);
                    Toast.makeText(getApplicationContext(),"인증번호 버튼을 한 번 더 눌러주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mWithoutDash, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        Button buttonCertif = (Button) findViewById(R.id.button_certif);
        buttonCertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 인증번호 가져오기
                String userVerificationCode = editPhoneCertifNum.getText().toString();
                // 인증번호 일치 여부 확인
                if (userVerificationCode.equals(generatedVerificationCode)) {
                    Toast.makeText(Login_idfind.this, "인증 완료", Toast.LENGTH_SHORT).show();
                    certif_result = 1;
                } else {
                    Toast.makeText(Login_idfind.this, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    certif_result = 2;
                }
            }
        });

        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // 자동 인증이 완료된 경우 처리
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editPhoneCertifNum.setText(code);
                    verifyPhoneNumberWithCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // 인증 실패 처리
                Log.d(TAG, "get failed with "+ e.getMessage());
                Toast.makeText(Login_idfind.this, "실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Random random = new Random();
                int randomNumber = random.nextInt(999999);
                s = String.valueOf(randomNumber);
                // 인증번호가 전송된 경우 처리
                verificationId = s;
                Toast.makeText(Login_idfind.this, "인증번호가 전송되었습니다", Toast.LENGTH_SHORT).show();
            }
        };

        TextView findpw = findViewById(R.id.text_findpw);
        findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pw = new Intent(Login_idfind.this, Login_pwfind.class);
                startActivity(intent_pw);
            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationCode) {
        // 사용자가 입력한 인증번호와 Firebase에 전송된 인증번호 일치 여부 확인
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // Firebase 인증 진행
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login_idfind.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 인증 성공
                            Toast.makeText(Login_idfind.this, "인증 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 인증 실패
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(Login_idfind.this, "fail: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void findId(View view) {
        if (editUserName.length()!=0 && editUserPhoneNum.length() != 0 && editPhoneCertifNum.length() != 0 && certif_result==1) {
            Task<QuerySnapshot> docRef = firestore.collection("사용자DB").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String email="";
                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                        String name = ds.get("name").toString();
                        String phoneNum  = ds.get("phone_number").toString();
                        if (editUserName.getText().toString().equals(name) && editUserPhoneNum.getText().toString().equals(phoneNum)) {
                            email = ds.get("email").toString();
                            Log.d(TAG, "사용자 이메일: "+email);
                            Intent intent_foundid = new Intent(Login_idfind.this, foundId.class);
                            intent_foundid.putExtra("email", email);
                            startActivity(intent_foundid);
                        } else {
                            Log.d(TAG, "일치하는 사용자 정보가 없습니다.");
                            Toast.makeText(Login_idfind.this, "일치하는 사용자 정보가 없습니다. ", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }else {
            Toast.makeText(Login_idfind.this, "아직 작성하지 않은 빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}