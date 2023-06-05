package com.example.bigmap.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bigmap.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class board_notice extends AppCompatActivity {

    private ListView listView;
    private noticelist_Adapter adapter;
    private List<noticelist_item> itemList;
    private FirebaseFirestore db;

    private static final String TAG = "board_notice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_notice);

        listView = findViewById(R.id.notice_List);
        itemList = new ArrayList<>();
        adapter = new noticelist_Adapter(board_notice.this, itemList);
        listView.setAdapter(adapter);

        // Firestore 초기화
        db = FirebaseFirestore.getInstance();

        // 최신 게시글 가져오기
        fetchLatestPosts();

        // 리스트뷰 아이템 클릭 이벤트 처리
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 클릭한 아이템의 동작 정의
            noticelist_item selectedItem = itemList.get(position);
            // 클릭한 아이템에 대한 동작 구현
            Intent intent = new Intent(getApplicationContext(), board_notice_detail.class);
            intent.putExtra("postId", selectedItem.getPostId());
            intent.putExtra("title", selectedItem.getTitle());
            intent.putExtra("user", selectedItem.getUser());
            intent.putExtra("time", selectedItem.getTime());
            startActivity(intent);
        });



        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchLatestPosts() {
        db.collection("공지사항DB")
                .orderBy("작성_시간_날짜", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String postId = document.getString("postId"); // Firestore 문서의 postId 가져오기
                            String title = document.getString("제목");
                            String user = document.getString("작성자");
                            String time = document.getString("작성_시간_날짜");
                            noticelist_item item = new noticelist_item(postId, title, user, time);
                            itemList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "새로고침을 실패하였습니다. ", task.getException());
                    }
                });
    }
}
