package com.example.bigmap.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigmap.R;
import com.example.bigmap.databinding.ActivityUserCheckBinding;

public class UserCheck extends AppCompatActivity {

    // 개인정보 동의 관련 체크박스 페이지. 필수를 모두 체크하지 않으면 다음 페이지로 넘어갈 수 없게 제작.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserCheckBinding binding = ActivityUserCheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CheckBox checked1 = findViewById(R.id.checkbox1);
        CheckBox checked2 = findViewById(R.id.checkbox2);
        CheckBox checked3 = findViewById(R.id.checkbox3);

        binding.buttonOk.setOnClickListener(view -> {
            if (checked1.isChecked() && checked2.isChecked() && checked3.isChecked()) {
//                binding.buttonOk.setEnabled(true);
                Intent intent_register = new Intent(this, PersonIform.class);
//                Intent intent1 = new Intent(this, PersonIform.class);
//                Intent intent1 = new Intent(this, PersonInform.class);
                startActivity(intent_register);
            } else {
                Toast.makeText(getApplicationContext(), "모든 필수 항목에 체크해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });
    }
}