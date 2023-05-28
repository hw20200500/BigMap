package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class board_free_detail extends AppCompatActivity {

    private TextView titleTextView;
    private TextView userTextView;
    private TextView timeTextView;
    private TextView contentTextView;
    private ListView commentListView;
    private EditText commentEditText;
    private Button commentRegButton;


    private comment_Adapter commentAdapter;
    private List<comment_item> commentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_free_detail);

        db = FirebaseFirestore.getInstance();

        titleTextView = findViewById(R.id.view_title);
        userTextView = findViewById(R.id.user);
        timeTextView = findViewById(R.id.time_text_view);
        contentTextView = findViewById(R.id.content_text_view);
        commentListView = findViewById(R.id.ListView);
        commentEditText = findViewById(R.id.comment);
        commentRegButton = findViewById(R.id.btn_comment_reg);

        commentList = new ArrayList<>();
        commentAdapter = new comment_Adapter(this, commentList);
        commentListView.setAdapter(commentAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        // 게시물 데이터 받아오기
        String postId = getIntent().getStringExtra("postId");
        db.collection("게시판DB")
                .document(postId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("제목");
                        String writeruser = documentSnapshot.getString("작성자");
                        String time = documentSnapshot.getString("작성_시간_날짜");
                        String content = documentSnapshot.getString("내용");

                        titleTextView.setText(title);
                        userTextView.setText(writeruser);
                        timeTextView.setText(time);
                        contentTextView.setText(content);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_free_detail.this, "게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });



        // 댓글 등록 버튼 클릭 이벤트 처리
        commentRegButton.setOnClickListener(v -> {



            if (user != null) {
                String commentUser = user.getUid(); // 현재 로그인한 사용자의 UID 가져오기
                String commentText = commentEditText.getText().toString().trim();

                // 댓글 내용이 비어있는지 확인
                if (!commentText.isEmpty()) {
                    registerComment(commentUser, commentText);
                } else {
                    Toast.makeText(board_free_detail.this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
// 초기화
                commentEditText.setText("");

            } else {
                // 사용자가 로그인되지 않은 경우, 로그인 화면을 표시하거나 메시지를 표시할 수 있습니다.
                Toast.makeText(board_free_detail.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        fetchComments();
    }

    private void registerComment(String commentUser, String commentText) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String commentUserId = user.getUid();
            String email = user.getEmail();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(calendar.getTime());

            DocumentReference userRef = db.collection("사용자DB").document(email);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");

                        // Create a new comment document in Firestore
                        String postId = getIntent().getStringExtra("postId");
                        DocumentReference commentRef = db.collection("게시판DB").document(postId).collection("댓글").document();

                        Map<String, Object> comment = new HashMap<>();
                        comment.put("작성자", name);
                        comment.put("내용", commentText);
                        comment.put("작성_날짜_시간", time);

                        commentRef.set(comment)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(board_free_detail.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    fetchComments();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(board_free_detail.this, "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            });
        } else {
            // User is not logged in
            Toast.makeText(board_free_detail.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchComments() {
        String postId = getIntent().getStringExtra("postId");
        // Fetch the comment list from Firestore
        db.collection("게시판DB").document(postId).collection("댓글")
                .orderBy("작성_날짜_시간", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    commentList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String commentUser = documentSnapshot.getString("작성자");
                        String commentText = documentSnapshot.getString("내용");
                        String commentTime = documentSnapshot.getString("작성_날짜_시간");

                        comment_item commentItem = new comment_item(commentUser, commentText, commentTime);
                        commentList.add(commentItem);
                    }
                    commentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_free_detail.this, "댓글 목록을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }
}
