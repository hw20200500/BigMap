package com.example.bigmap.login_register;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Random;


public class PersonIform extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String verificationId;
    private String mVerificationId;
    private String mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private EditText editPhonenum;
    private String generatedVerificationCode;
    private EditText verificationCodeEditText;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private int certif_result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_iform);

//        ActivityPersonIformBinding binding = ActivityPersonIformBinding.inflate(getLayoutInflater());

//        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        EditText editUserMail = findViewById(R.id.userEmail);
        EditText editUserName = findViewById(R.id.userName);
        EditText editUserPhoneNum = findViewById(R.id.userPhoneNum);
        EditText editPhoneCertifNum = findViewById(R.id.phoneCertif_Num);
        EditText userpw = findViewById(R.id.userPassword);
        EditText userpw_check = findViewById(R.id.user_pw_check);
        Spinner spinnerY = findViewById(R.id.userBirth_year);
        Spinner spinnerM = findViewById(R.id.userBirth_month);
        Spinner spinnerD = findViewById(R.id.userBirth_day);



        //이메일 주소 입력
        Spinner spinnermails = findViewById(R.id.Emailaddr);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.emailaddr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermails.setAdapter(adapter);


        spinnermails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    if (editUserMail.length()!=0) {
                        String editmail = editUserMail.getText().toString();
                        if (editmail.contains("@")) {
                            String[] str = editmail.split("@");
                            editUserMail.setText(str[0]);
                        }
                    }
                    editUserMail.append(spinnermails.getSelectedItem().toString());
//                    binding.userEmail.append(spinnermails.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("메일주소를 선택하세요");
            }
        });

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


        int Y = 2023;
        Object[] BirthY = new Object[101];
        for (int i = 1; i <= 100; i++) {
            BirthY[i] = Y;
            Y--;
        }

        int M = 1;
        Object[] BirthM = new Object[13];
        for (int i = 1; i <= 12; i++) {
            BirthM[i] = M;
            M++;
        }

        int D = 1;
        Object[] BirthD = new Object[32];
        for (int i = 1; i <= 31; i++) {
            BirthD[i] = D;
            D++;
        }

//출생연도 스피너
        ArrayAdapter<Object> adapterY = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthY);
        adapterY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerY.setAdapter(adapterY);

        spinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(this@person_inform, BirthY[position], Toast.LENGTH_SHORT).show()*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("연도");
            }
        });

//월 스피너
        ArrayAdapter<Object> adapterM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthM);
        spinnerM.setAdapter(adapterM);

        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("월");
            }
        });

//일 스피너
        ArrayAdapter<Object> adapterD = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BirthD);
        spinnerD.setAdapter(adapterD);

        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("일");
            }
        });

        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        editUserPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        binding.userPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        EditText phoneNumber = findViewById(R.id.userPhoneNum);
        Button buttonSend = (Button) findViewById(R.id.button_num_send);
        verificationCodeEditText = findViewById(R.id.phoneCertif_Num);
        editPhonenum = findViewById(R.id.userPhoneNum);
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
                if (ContextCompat.checkSelfPermission(PersonIform.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(PersonIform.this,
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
                /*String verificationCode = editPhoneCertifNum.getText().toString();
                verifyPhoneNumberWithCode(verificationCode);*/
                // 입력된 인증번호 가져오기
                String userVerificationCode = verificationCodeEditText.getText().toString();
                // 인증번호 일치 여부 확인
                if (userVerificationCode.equals(generatedVerificationCode)) {
                    Toast.makeText(PersonIform.this, "인증 완료", Toast.LENGTH_SHORT).show();
                    certif_result = 1;
                } else {
                    Toast.makeText(PersonIform.this, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PersonIform.this, "실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Random random = new Random();
                int randomNumber = random.nextInt(999999);
                s = String.valueOf(randomNumber);
                // 인증번호가 전송된 경우 처리
                verificationId = s;
                Toast.makeText(PersonIform.this, "인증번호가 전송되었습니다", Toast.LENGTH_SHORT).show();
            }
        };

        Button buttoncancel = (Button) findViewById(R.id.button_cancel);
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_cancel = new Intent(PersonIform.this, Login.class);
                startActivity(intent_cancel);
            }
        });


        Button buttonPIFinsh = (Button) findViewById(R.id.button_PI_Finish);
        //회원가입 버튼 클릭: EditText가 모두 채워지지 않았다면 '~을 작성하세요' 문구 출력, 그렇지 않으면 파이어베이스 사용자 정보 및 데이터베이스(파이어스토어)에 회원 정보 저장
        buttonPIFinsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUserMail.length() != 0 && editUserName.length() != 0 && userpw.length() != 0 && userpw_check.length() != 0 && spinnerD.getSelectedItemPosition() != 0 && spinnerM.getSelectedItemPosition() != 0 && spinnerY.getSelectedItemPosition() != 0&& editUserPhoneNum.length() != 0 && editPhoneCertifNum.length() != 0 && certif_result ==1) {
                   buttonPIFinsh.setEnabled(true);

                            //signup(); edittext정보 문자화해서 변수에 저장
                    String userEmail = editUserMail.getText().toString();
                    String userPassword = userpw.getText().toString();
                    String userName = editUserName.getText().toString();

                    int userBirth_year = Integer.parseInt(spinnerY.getSelectedItem().toString());
                    int userBirth_month = Integer.parseInt(spinnerM.getSelectedItem().toString());
                    int userBirth_day = Integer.parseInt(spinnerD.getSelectedItem().toString());
                            /*String userBirth_year = spinnerY.getSelectedItem().toString().trim();
                            String userBirth_month = spinnerM.getSelectedItem().toString().trim();
                            String userBirth_day = spinnerD.getSelectedItem().toString().trim();*/
                    String userPhoneNum = editUserPhoneNum.getText().toString().trim();


                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(PersonIform.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //가입 성공시
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String email = user.getEmail();
                                        String uid = user.getUid();


                                        //해쉬맵 테이블을 파이어베이스 데이터베이스(파이어스토어)에 저장
                                        HashMap<Object,Object> hashMap = new HashMap<>();

                                        hashMap.put("email",email);
                                        hashMap.put("password",userPassword);
                                        hashMap.put("name",userName);
                                        hashMap.put("birth_year",userBirth_year);
                                        hashMap.put("birth_month",userBirth_month);
                                        hashMap.put("birth_day",userBirth_day);
                                        hashMap.put("phone_number",userPhoneNum);
                                        hashMap.put("uid",uid);
                                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                            firestore.collection("사용자DB").document(email).set(hashMap);


                                            //가입이 이루어져을시 가입 화면을 빠져나감.
                                            Toast.makeText(PersonIform.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PersonIform.this, Login.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                            Toast.makeText(PersonIform.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                            return;  //해당 메소드 진행을 멈추고 빠져나감.

                                        }

                                    }
                                })
                                    // 새 계정 만드는 활동 자체가 실패(에러)했을 경우
                                    .addOnFailureListener(PersonIform.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PersonIform.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });


                            // Send the signup data to the server using a HTTP POST request: php 파일이 있는 웹 링크로 이동
                            /*String url = "http://172.30.1.17/register_bigmap.php";
                            RequestQueue queue = Volley.newRequestQueue(PersonIform.this);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent1);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    String errorMessage = "Unknown error occurred.";
                                    if (error.networkResponse != null && error.networkResponse.data != null) {
                                        errorMessage = new String(error.networkResponse.data, Charset.defaultCharset());
                                    }
                                    Log.e(TAG, "Error during sign up: " + errorMessage);
                                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                //입력된 개인정보를 모두 데이터베이스에 저장하기
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userEmail", userEmail);
                                    params.put("userPassword", userPassword);
                                    params.put("userName", userName);
                                    params.put("userBirth_year", userBirth_year);
                                    params.put("userBirth_month", userBirth_month);
                                    params.put("userBirth_day", userBirth_day);
                                    params.put("userPhoneNum", userPhoneNum);
                                    return params;
                                }
                            };

                            queue.add(stringRequest);*/

                            //모든 edittext에 입력하지 않았을 경우
                } else {
                    if (editUserMail.length() == 0) {
                        Toast.makeText(getApplicationContext(), "이메일을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userpw.length() == 0) {
                            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (userpw_check.length() == 0) {
                                Toast.makeText(getApplicationContext(), "'비밀번호 확인'란을 작성해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!userpw.getText().toString().equals(userpw_check.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (editUserName.length() == 0) {
                                        Toast.makeText(getApplicationContext(), "이름을 작성해주세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (spinnerD.getSelectedItemPosition() == 0 || spinnerM.getSelectedItemPosition() == 0 || spinnerY.getSelectedItemPosition() == 0) {
                                            Toast.makeText(getApplicationContext(), "생년월일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (editPhonenum.length() == 0) {
                                                Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (editPhoneCertifNum.length() == 0) {
                                                    Toast.makeText(getApplicationContext(), "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (certif_result == 0) {
                                                        Toast.makeText(getApplicationContext(), "인증번호 확인 버튼을 클릭하세요.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (certif_result == 2){
                                                            Toast.makeText(getApplicationContext(), "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

    });
    }

    private void go_before() {
        Intent intent_before = new Intent(this, UserCheck.class);
        startActivity(intent_before);
    }
    private void sendVerificationCode(String phoneNum) {
        // 전화번호 가져오기
        String phoneNumber = editPhonenum.getText().toString();
        Random random = new Random();
        int randomNumber = random.nextInt(999999);

        // 인증번호 생성
        generatedVerificationCode = String.valueOf(randomNumber);

        // 문자 전송 로직 (여기서는 단순히 Toast 메시지로 대체)
        PendingIntent Pi = PendingIntent.getActivities(this, 0, new Intent[]{new Intent(PersonIform.this, PersonIform.class)}, 0);
        SmsManager sms = SmsManager.getDefault();
        String message = "BigMap의 인증번호는 "+randomNumber+"입니다.";
        sms.sendTextMessage(phoneNum, null, message, Pi, null);
        Toast.makeText(this, "인증번호 전송 완료", Toast.LENGTH_SHORT).show();
    }

    private int verifyCode() {
        // 입력된 인증번호 가져오기
        String userVerificationCode = verificationCodeEditText.getText().toString();
        int result;
        // 인증번호 일치 여부 확인
        if (userVerificationCode.equals(generatedVerificationCode)) {
            Toast.makeText(this, "인증 완료", Toast.LENGTH_SHORT).show();
            result = 1;
        } else {
            Toast.makeText(this, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            result = 0;
        }
        return result;
    }

    /*private void sendSms() {
        // 전화번호 가져오기
        String phoneNumber = editPhonenum.getText().toString();

        // SMS 전송 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
            return;
        }

        // SMS 전송
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "안녕하세요", null, null);
            Toast.makeText(this, "문자가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "문자 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인된 경우 SMS 전송
                sendSms();
            } else {
                Toast.makeText(this, "SMS 전송 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    /*private void sendVerificationCode(String phoneNumber) {
        try {
            // 전화번호 유효성 검사 및 E.164 형식으로 변환
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "KR"); // 국가 코드에 맞게 변경 필요
            String formattedPhoneNumber = phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
            Toast.makeText(PersonIform.this, formattedPhoneNumber, Toast.LENGTH_SHORT).show();

            // Firebase 인증 요청
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    formattedPhoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    PersonIform.this,
                    verificationCallbacks
            );
        } catch (NumberParseException e) {
            e.printStackTrace();
            // 전화번호 형식이 잘못된 경우 에러 처리
        }
    }*/

    private void verifyPhoneNumberWithCode(String verificationCode) {
        // 사용자가 입력한 인증번호와 Firebase에 전송된 인증번호 일치 여부 확인
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // Firebase 인증 진행
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(PersonIform.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 인증 성공
                            Toast.makeText(PersonIform.this, "인증 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 인증 실패
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(PersonIform.this, "fail: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void mCallBacks() {

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = String.valueOf(token);
            }
        };
        /*mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInProcess(phoneAuthCredential);
                String sms = phoneAuthCredential.getSmsCode();
                System.err.println("Check!!!!!!!!!!!!!!! "+sms);
                if (sms != null){
                    binding.otpView.setText(sms);
                }
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PersonIform.this, "OTPError "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verificationId = s;
                        System.out.println("Verification Id : !!!!!! "+verificationId);
                    }
                }, 10000);
            }
        };*/
    }





}

