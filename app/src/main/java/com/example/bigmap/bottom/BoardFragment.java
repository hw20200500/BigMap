package com.example.bigmap.bottom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigmap.R;
import com.example.bigmap.board.board_free;
import com.example.bigmap.board.board_notice;
import com.example.bigmap.board.board_qna;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    private ListView freeList;
    private ListView noticeList;
    private ListView qnaList;
    private ArrayAdapter<String> freeAdapter;
    private ArrayAdapter<String> noticeAdapter;
    private ArrayAdapter<String> qnaAdapter;
    private List<String> freeItemList;
    private List<String> noticeItemList;
    private List<String> qnaItemList;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        firestore = FirebaseFirestore.getInstance();

        freeList = view.findViewById(R.id.free_List);
        noticeList = view.findViewById(R.id.notice_List);
        qnaList = view.findViewById(R.id.qna_List);

        freeItemList = new ArrayList<>();
        noticeItemList = new ArrayList<>();
        qnaItemList = new ArrayList<>();

        freeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, freeItemList);
        noticeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, noticeItemList);
        qnaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, qnaItemList);

        freeList.setAdapter(freeAdapter);
        noticeList.setAdapter(noticeAdapter);
        qnaList.setAdapter(qnaAdapter);

        TextView noticeButton = view.findViewById(R.id.notice);
        TextView qnaButton = view.findViewById(R.id.qna);
        TextView freeButton = view.findViewById(R.id.free);

        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 공지사항 화면으로 전환
                Intent intent = new Intent(getActivity(), board_notice.class);
                startActivity(intent);
            }
        });

        qnaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Q&A 화면으로 전환
                Intent intent = new Intent(getActivity(), board_qna.class);
                startActivity(intent);
            }
        });

        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 자유게시판 화면으로 전환
                Intent intent = new Intent(getActivity(), board_free.class);
                startActivity(intent);
            }
        });

        // 최신 게시물 가져오기
        loadLatestPosts();

        return view;
    }

    private void loadLatestPosts() {
        // 자유게시판 최신 게시물 가져오기 (최대 5개)
        firestore.collection("게시판DB")
                .whereEqualTo("게시판 종류", "자유게시판")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {freeItemList.clear();
                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                    {
                        String title=documentSnapshot.getString("제목");
                        freeItemList.add(title);
                    }
                    freeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "자유게시판 게시물을 가져오는데 실패했습니다.",
                            Toast.LENGTH_SHORT).show();
                });
        // 공지사항 최신 게시물 가져오기 (최대 3개)
        firestore.collection("게시판DB")
                .whereEqualTo("게시판 종류", "공지사항")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    noticeItemList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("제목");
                        noticeItemList.add(title);
                    }
                    noticeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "공지사항 게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });

        // Q&A 최신 게시물 가져오기 (최대 3개)
        firestore.collection("게시판DB")
                .whereEqualTo("게시판 종류", "Q&A")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    qnaItemList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("제목");
                        qnaItemList.add(title);
                    }
                    qnaAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Q&A 게시물을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }

}