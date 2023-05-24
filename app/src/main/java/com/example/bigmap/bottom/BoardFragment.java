package com.example.bigmap.bottom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bigmap.R;
import com.example.bigmap.board.board_chat;
import com.example.bigmap.board.board_free;
import com.example.bigmap.board.board_notice;
import com.example.bigmap.board.board_qna;
import com.example.bigmap.databinding.FragmentBoardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.http.Query;


public class BoardFragment extends Fragment {
    private View view;
    private ListView free_list;
    private ArrayAdapter<String> adapter;
    private List<String> itemList;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

   //사용자 건의함 페이지

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);



        free_list = view.findViewById(R.id.free_List);
        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, itemList);
        free_list.setAdapter(adapter);


        FragmentBoardBinding binding = FragmentBoardBinding.inflate(getLayoutInflater());

        return view;
    }

    public void updateFreeList(List<String> freeList){
        itemList.clear();

        //최신글로 5개 나열
        int count = Math.min(freeList.size(), 5);
        for(int i = 0; i<count;i++){
            itemList.add(free_list.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

 // 버튼 id로 찾음
        TextView qnaButton = view.findViewById(R.id.qna);
        TextView noticeButton = view.findViewById(R.id.notice);
        TextView freeButton = view.findViewById(R.id.free);



        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        DocumentReference postRef = firestore.collection("게시판DB").document(email);

        private void loadLatestPosts(){
            postRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(3).get().addOnSuccessListener(queryDocumenetSnapshots ->{
                List<Post> latestPosts = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumenetSnapshots){
                    Post post = documentSnapshot.toObject(board_free_detail.class);
                    latestPosts.add(post);
                }

            });
        }



        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), board_free.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), board_notice.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        qnaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), board_qna.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });


    }
}