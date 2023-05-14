package com.example.bigmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.checkerframework.checker.units.qual.Length;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText search_bar = (EditText) findViewById(R.id.edittext_search);
        LinearLayout recent_view = (LinearLayout) findViewById(R.id.recent_view);
        LinearLayout loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);

        // edittext의 상태 동적 할당 받음 -> edittext에 글자 입력하면 위치 관련 정보 레이아웃이 보이도록함
        // -> 입력한 글자가 없으면 최근 검색어 레이아웃이 보이도록 함.
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void delete_text() {
        // 최근 검색어 레이아웃의 'X' 이미지 눌렀을 때, 내용물 삭제되는 함수 코드 작성
    }

    public void go_location() {
        // 최근 검색어 레이아웃, 관련 위치 정보 레이아웃 클릭했을 때, 해당 장소 정보 페이지, 혹은 네비게이션 안내 페이지로 이동하는 코드 작성
    }
}