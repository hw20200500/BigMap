package com.example.bigmap;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.bigmap.databinding.FragmentBoardBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends Fragment {
    ImageView imageView;

    //마이페이지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentBoardBinding binding = FragmentBoardBinding.inflate(getLayoutInflater());

        return inflater.inflate(R.layout.fragment_mypage, container, false);
    }
    
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button_nameC = view.findViewById(R.id.button_nameC); // 버튼 id로 찾음
        Button button_phoneC = view.findViewById(R.id.button_phoneC);
        Button button_emailC = view.findViewById(R.id.button_emailC);
        Button button_birthC = view.findViewById(R.id.button_birthC);

        button_nameC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_name_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_phoneC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_phonnumCh.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_emailC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_email_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_birthC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_birth_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });
    }
}
