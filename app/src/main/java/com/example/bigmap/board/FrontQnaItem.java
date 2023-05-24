package com.example.bigmap.board;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigmap.R;

public class FrontQnaItem extends LinearLayout {
    private TextView textViewTitle;

    public FrontQnaItem(Context context) {
        super(context);
        init(context);
    }

    public FrontQnaItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FrontQnaItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.front_qna_item, this);
        textViewTitle = findViewById(R.id.view_title);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }
}
