package com.example.bigmap.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;

import java.util.ArrayList;
import java.util.List;

public class board_notice extends AppCompatActivity {

    private ListView noticeList;
    private ArrayAdapter<String> noticeAdapter;
    private List<String> noticeItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_notice);

        noticeList = findViewById(R.id.notice_List);
        noticeItemList = new ArrayList<>();
        noticeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noticeItemList);
        noticeList.setAdapter(noticeAdapter);

        // 공지사항 아이템 클릭 이벤트 처리
        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = noticeItemList.get(position);
                Toast.makeText(board_notice.this, "Selected Item: " + selectedItem, Toast.LENGTH_SHORT).show();
                // 클릭된 아이템
                noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = noticeItemList.get(position);
                        // 선택된 아이템의 정보를 다른 액티비티로 전달하기 위한 인텐트 생성
                        Intent intent = new Intent(board_notice.this, board_free_detail.class);
                        intent.putExtra("selectedItem", selectedItem); // 선택된 아이템의 정보를 인텐트에 추가

                        startActivity(intent); // 다른 액티비티로 전환
                    }
                });
            }
        });

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

    }
}
