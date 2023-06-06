package com.example.bigmap.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.bigmap.R;

public class Bottom_home_sub extends LinearLayout {

    public Bottom_home_sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Bottom_home_sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_mainbottom_sub,this,true);
    }
}
