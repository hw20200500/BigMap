package com.example.bigmap.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class board_free_detail extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private TextView titleTextView;
    private TextView userTextView;
    private TextView timeTextView;
    private TextView contentTextView;
    private ListView commentListView;
    private EditText commentEditText;
    private Button commentRegButton;

    private FirebaseFirestore firestore;
    private comment_Adapter commentAdapter;
    private List<Map<String, String>> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_free_detail);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

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

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        // 게시물 데이터 받아오기
        String postId = getIntent().getStringExtra("postId");
        firestore.collection("게시판DB")
                .document(postId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("제목");
                        String user = documentSnapshot.getString("작성자");
                        String time = documentSnapshot.getString("시간");
                        String content = documentSnapshot.getString("내용");

                        titleTextView.setText(title);
                        userTextView.setText(user);
                        timeTextView.setText(time);
                        contentTextView.setText(content);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_free_detail.this, "게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });

        // 댓글 등록 버튼 클릭 이벤트 처리
        commentRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
            }
        });

        fetchComments();
    }

    private void registerComment(String commentUser, String commentText) {
        // Create a new comment document in Firestore
        Map<String, Object> comment = new HashMap<>();
        comment.put("user", commentUser);
        comment.put("comment", commentText);

        firestore.collection("댓글")
                .add(comment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(board_free_detail.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    fetchComments(); // Fetch the updated comment list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_free_detail.this, "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchComments() {
        // Fetch the comment list from Firestore
        firestore.collection("댓글")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    commentList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String commentUser = documentSnapshot.getString("user");
                        String commentText = documentSnapshot.getString("comment");

                        Map<String, String> comment = new HashMap<>();
                        comment.put("user", commentUser);
                        comment.put("comment", commentText);

                        commentList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(board_free_detail.this, "댓글 목록을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }
}
