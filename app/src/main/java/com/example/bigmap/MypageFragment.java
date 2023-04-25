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
}