package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class board_free extends AppCompatActivity {

    private ListView listView;
    private freelist_Adapter adapter;
    private List<freelist_item> itemList;
    private FirebaseFirestore db;
    private Button writeButton;
    private static final String TAG = "board_free";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_free);

        listView = findViewById(R.id.free_List);
        itemList = new ArrayList<>();
        adapter = new freelist_Adapter(board_free.this, itemList);
        listView.setAdapter(adapter);

        // Firestore 초기화
        db = FirebaseFirestore.getInstance();

        // 최신 게시글 가져오기
        fetchLatestPosts();

        // 리스트뷰 아이템 클릭 이벤트 처리
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 클릭한 아이템의 동작 정의
            freelist_item selectedItem = itemList.get(position);
            // 클릭한 아이템에 대한 동작 구현
            Intent intent = new Intent(getApplicationContext(), board_free_detail.class);
            intent.putExtra("title", selectedItem.getTitle());
            intent.putExtra("user", selectedItem.getUser());
            intent.putExtra("time", selectedItem.getTime());
            startActivity(intent);
        });

        // writeButton를 id를 이용하여 가져옵니다.
        writeButton = findViewById(R.id.write);
        //글쓰기 버튼 클릭시 이동
        writeButton.setOnClickListener(view -> {
            Intent intent = new Intent(board_free.this, board_write.class);
            startActivity(intent);
        });

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

    }


    private void fetchLatestPosts() {
        db.collection("게시판DB")
                .orderBy("작성_시간_날짜", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("제목");
                            String user = document.getString("작성자");
                            String time = document.getString("작성_시간_날짜");
                            freelist_item item = new freelist_item(title, user, time);
                            Log.d(TAG, "제목: "+title);
                            itemList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "새로고침을 실패하였습니다. ", task.getException());
                    }
                });
    }


}
