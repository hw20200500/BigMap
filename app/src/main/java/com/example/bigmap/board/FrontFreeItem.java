package com.example.bigmap.board;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigmap.R;

public class FrontFreeItem extends LinearLayout {
    private TextView textViewTitle;

    public FrontFreeItem(Context context) {
        super(context);
        init(context);
    }

    public FrontFreeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FrontFreeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.front_free_item, this);
        textViewTitle = findViewById(R.id.view_title);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }
}
