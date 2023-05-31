package com.example.bigmap;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class search_sub extends LinearLayout {

    public search_sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public search_sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_favorite_sub,this,true);
    }


}