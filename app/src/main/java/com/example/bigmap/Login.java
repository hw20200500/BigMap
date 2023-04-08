package com.example.bigmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText Email, Password;
    Button loginButton, newuserButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 로그인 이메일, 비번 입력창 변수 저장
        Email = findViewById(R.id.userEmail);
        Password = findViewById(R.id.userPassword);

        // 로그인 버튼, 회원가입 버튼
        loginButton = (Button) findViewById(R.id.button_login); // 로그인
        newuserButton = (Button) findViewById(R.id.button_new_user); // 회원가입


        // 로그인 버튼 클릭
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext에 입력된 이메일과 비번을 String 형태로 변환 및 변수에 저장.
                String userEmail = Email.getText().toString().trim();
                String userPassword = Password.getText().toString().trim();


                //php파일이 연결된 링크로 가서 데이터베이스에 접근. 만일 데이터베이스에 없는 이메일 주소, 비밀번호이거나, 사용자의 이메일 주소와 비밀번호가 일치하지 않는 경우, 경고 문구 출력, 그렇지 않으면 로그인 완료.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.45.170/login_bigmap.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                try {
                                    if (jsonObject.getString("status").equals("success")) {
                                        // Login successful, go to home activity
                                        JSONObject userData = jsonObject.getJSONObject("data");
                                        String userName = userData.getString("userName");
                                        String userEmail = userData.getString("userEmail");
                                        String userPhoneNum = userData.getString("userPhoneNum");
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Login failed, show error message
                                        Toast.makeText(Login.this, "이메일 주소 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userEmail", userEmail);
                        params.put("userPassword", userPassword);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);
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
    }

    }