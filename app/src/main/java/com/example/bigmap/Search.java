package com.example.bigmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void delete_text() {
        // 'X' 이미지 눌렀을 때, 내용물 삭제되는 함수 코드 작성
    }

    public void go_location() {
        // 최근 검색어 레이아웃 클릭했을 때, 해당 장소 정보 페이지, 혹은 네비게이션 안내 페이지로 이동하는 코드 작성
    }
}