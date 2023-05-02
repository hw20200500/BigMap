package com.example.bigmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigmap.databinding.FragmentBoardBinding;


public class BoardFragment extends Fragment {
    private View view;


    //마이페이지
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentBoardBinding binding = FragmentBoardBinding.inflate(getLayoutInflater());

        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button chatButton = view.findViewById(R.id.chat); // 버튼 id로 찾음
        Button qnaButton = view.findViewById(R.id.qna);
        Button noticeButton = view.findViewById(R.id.notice);
        Button freeButton = view.findViewById(R.id.free);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), board_chat.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

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
