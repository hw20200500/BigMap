package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skt.tmap.engine.navigation.network.NetworkRequester;



import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.http.Query;

public class board_free extends AppCompatActivity {

    ArrayList<freelist_item> freeItemList;

    final private String TAG = getClass().getSimpleName();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> itemList;
    private FirebaseFirestore db;
    static boolean calledAlready = false;
    private Button writeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_free);

        if (calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnanled(true);
            calledAlready = true;
        }

        listView = findViewById(R.id.free_List);
        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        //Firestore  초기화
        db = FirebaseFirestore.getInstance();

        // 최신 게시글 가져오기
        fetchLatestPosts();

        // 리스트뷰 아이템 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템의 동작 정의
                //freelist_Adapter selectedItem = freelist_Adapter.getItem(position);
                freelist_Adapter adapter = new freelist_Adapter(this, freelist_item.class, itemList);
                listView.setAdapter(adapter);
                // TODO: 클릭한 아이템에 대한 동작 구현
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), board_free_detail.class);
                        /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                        intent.putExtra("title", Integer.toString(itemList.get(position).getTitle()));
                        intent.putExtra("user", itemList.get(position).getUser());
                        intent.putExtra("time", itemList.get(position).getTime());
                        startActivity(intent);
                    }
                });

                adapter.notifyDataSetChanged();

            }

        });


        // writeButton을 id를 이용하여 가져옵니다.
        writeButton = findViewById(R.id.write);
        //글쓰기 버튼 클릭시 이동
        writeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(board_free.this, board_write.class);
                startActivity(intent);

            }
        });


        private void fetchLatestPosts () {
            // 최신 게시글을 가져오는 로직을 구현합니다.
            // 데이터베이스 또는 서버 API와의 통신을 통해 최신 게시글을 가져올 수 있습니다.
            // 가져온 게시글의 제목을 itemList에 추가합니다.
            // 아래는 예시로 간단히 itemList에 몇 개의 게시글 제목을 추가하는 코드입니다.
            db.collection("게시판DB")
                    .orderBy("timestamp", Query.Direction.DESCENDING)

                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(t @NonNull Task<QuerySnapshot>task) {
                            if (task.isSuccessful()) {
                                itemList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    String title = document.getString("제목");
                                    String user = document.getString("Document ID");
                                    String time = document.getString("작성 시간/날짜");

                                    freelist_item item = new freelist_item(title, user, time);
                                    itemList.add(item);
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }


    }
}