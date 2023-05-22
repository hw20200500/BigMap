package com.example.bigmap.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.login_register.PersonIform;
import com.example.bigmap.mapview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class mypage_phonnumCh extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    EditText editUserPhoneNum;
    private String generatedVerificationCode;
    private EditText verificationCodeEditText;
    private EditText editCertifNum;
    private EditText editPhonenum;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private int certif_result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_phonnum_ch);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        editCertifNum=findViewById(R.id.phoneCertif_Num);
//        binding.userPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        EditText phoneNumber = findViewById(R.id.userPhoneNum);
        Button buttonSend = findViewById(R.id.button_num_send);
        verificationCodeEditText = findViewById(R.id.phoneCertif_Num);
        editPhonenum = findViewById(R.id.userPhoneNum);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPhoneNum = editPhonenum.getText().toString().trim();
                // '-' 제거
//                String mWithoutDash = userPhoneNum.replace("-", "");
                Random random = new Random();
                int randomNumber = random.nextInt(999999);

                // 인증번호 생성
                generatedVerificationCode = String.valueOf(randomNumber);

                String sms = "[BigMap]\n전화번호 인증코드 : "+generatedVerificationCode;
                // SMS 전송 권한 확인
                if (ContextCompat.checkSelfPermission(mypage_phonnumCh.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(mypage_phonnumCh.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            SMS_PERMISSION_REQUEST_CODE);
                    Toast.makeText(getApplicationContext(),"인증번호 버튼을 한 번 더 눌러주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(userPhoneNum, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        Button buttonCertif = findViewById(R.id.button_certif);
        buttonCertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String verificationCode = editPhoneCertifNum.getText().toString();
                verifyPhoneNumberWithCode(verificationCode);*/
                // 입력된 인증번호 가져오기
                String userVerificationCode = verificationCodeEditText.getText().toString();
                // 인증번호 일치 여부 확인
                if (userVerificationCode.equals(generatedVerificationCode)) {
                    Toast.makeText(mypage_phonnumCh.this, "인증 완료", Toast.LENGTH_SHORT).show();
                    certif_result = 1;
                } else {
                    Toast.makeText(mypage_phonnumCh.this, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    certif_result = 2;
                }
            }
        });
        Button button_save = findViewById(R.id.button_save);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPhonenum.getText().length() != 0 && editCertifNum.getText().length() != 0) {

                    String phone_num = editPhonenum.getText().toString();
                    String certif_num = editCertifNum.getText().toString();
                    if (certif_result == 1) {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String email = user.getEmail();


                        DocumentReference docref=firestore.collection("사용자DB").document(email);
                        docref.update("phone_number", phone_num);

                        Toast.makeText(mypage_phonnumCh.this, "전화번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mypage_phonnumCh.this, mapview.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mypage_phonnumCh.this, "잘못된 인증번호입니다.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(mypage_phonnumCh.this, "전화번호 혹은 인증번호가 작성되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}