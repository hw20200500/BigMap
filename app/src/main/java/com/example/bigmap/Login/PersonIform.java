package com.example.bigmap.Login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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

import androidx.appcompat.app.AppCompatActivity;
//import com.example.bigmap.databinding.ActivityPersonIformBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bigmap.R;


import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class PersonIform extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_iform);

//        ActivityPersonIformBinding binding = ActivityPersonIformBinding.inflate(getLayoutInflater());

//        setContentView(binding.getRoot());

        EditText editUserMail = findViewById(R.id.userEmail);
        EditText editUserName = findViewById(R.id.userName);
        EditText editUserPhoneNum = findViewById(R.id.userPhoneNum);
        EditText PhoneCertifNum = findViewById(R.id.phoneCertif_Num);
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
                if (spinnermails.getSelectedItem().toString()!="직접 입력하기") {
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
                System.out.println("연도를 선택하세요");
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
            }
        });

        editUserPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        binding.userPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//        EditText phoneNumber = findViewById(R.id.userPhoneNum);
        Button buttonSend = (Button) findViewById(R.id.button_phoneCertif);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SMS_SEND},Permission_REQUEST_SMS);*/
            }
        });

        Button buttonPIFinsh = (Button) findViewById(R.id.button_PI_Finish);
        //회원가입 버튼 클릭: EditText가 모두 채워지지 않았다면 '~을 작성하세요' 문구 출력, 그렇지 않으면 php파일 링크로 이동하여 데이터베이스에 회원 정보 저장
        buttonPIFinsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUserMail.length() != 0 && editUserName.length() != 0 && editUserPhoneNum.length() != 0 && PhoneCertifNum.length() != 0 && userpw.length() != 0 && userpw_check.length() != 0 && spinnerD.getSelectedItemPosition() != 0 && spinnerM.getSelectedItemPosition() != 0 && spinnerY.getSelectedItemPosition() != 0) {
                   buttonPIFinsh.setEnabled(true);

                            //signup(); edittext정보 문자화해서 변수에 저장
                            String userEmail = editUserMail.getText().toString().trim();
                            String userPassword = userpw.getText().toString().trim();
                            String userName = editUserName.getText().toString().trim();
                            //int userBirth_year = Integer.parseInt(spinnerY.getSelectedItem().toString());
                            //int userBirth_month = Integer.parseInt(spinnerM.getSelectedItem().toString());
                            //int userBirth_day = Integer.parseInt(spinnerD.getSelectedItem().toString());
                            String userBirth_year = spinnerY.getSelectedItem().toString().trim();
                            String userBirth_month = spinnerM.getSelectedItem().toString().trim();
                            String userBirth_day = spinnerD.getSelectedItem().toString().trim();
                            String userPhoneNum = editUserPhoneNum.getText().toString().trim();

                            // Send the signup data to the server using a HTTP POST request: php 파일이 있는 웹 링크로 이동
                            String url = "http://192.168.45.172/register_bigmap.php";
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

                            queue.add(stringRequest);

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
                                            if (editUserPhoneNum.length() == 0) {
                                                Toast.makeText(getApplicationContext(), "휴대전화 번호를 작성해주세요.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (PhoneCertifNum.length() == 0) {
                                                    Toast.makeText(getApplicationContext(), "휴대전화를 인증해주세요.", Toast.LENGTH_SHORT).show();
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



}

